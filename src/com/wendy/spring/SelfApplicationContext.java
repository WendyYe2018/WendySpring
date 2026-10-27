package com.wendy.spring;

import com.wendy.spring.aop.MethodInterceptor;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfApplicationContext {

    private Class configClass;

    private static Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private static Map<String, Object> singletonObjects = new HashMap<>();

    private static List<BeanPostProcessor>
            beanPostProcessors =
            new ArrayList<BeanPostProcessor>();

    public static List<MethodInterceptor>
            methodInterceptors =
            new ArrayList<>();

    private static void ScanFolder(File file, String path, ClassLoader classLoader) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getName();
                if (f.isDirectory()) {
                    ScanFolder(f, path + "/" + fileName, classLoader);
                }
                else if (f.isFile() && f.getName().endsWith(".class")) {
                    String className = path.replace("/", ".") + "." + fileName.substring(0, fileName.length() - 6);
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);

                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            if (beanName.isEmpty()) {
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }


                            beanDefinitionMap.put(beanName, beanDefinition); // 存的是class有什么注解

                            if(BeanPostProcessor.class
                                    .isAssignableFrom(clazz)) {

                                BeanPostProcessor processor =
                                        (BeanPostProcessor)
                                                clazz.getDeclaredConstructor()
                                                        .newInstance();

                                beanPostProcessors.add(processor);
                            }

                            if (MethodInterceptor.class
                                    .isAssignableFrom(clazz)) {

                                MethodInterceptor interceptor =
                                        (MethodInterceptor)
                                                clazz.getDeclaredConstructor()
                                                        .newInstance();

                                methodInterceptors.add(
                                        interceptor
                                );
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    public SelfApplicationContext(Class aClass) {

        this.configClass = aClass;

        // 1.扫描ComponentScan指定的目录class， 得到很多的BeanDefinition, beanPostProcessors, 以及methodInterceptors
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value(); //扫描路径 com.wendy.service
            path = path.replace(".", "/"); // com/wendy/service
            ClassLoader classLoader = SelfApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            ScanFolder(file, path, classLoader);

        }

        // 2.扫描结束 把单例bean捞出来 直接构造
        // 2.0 无参构造函数实例化
        // 2.1 有可能有依赖autowired没办法getBean 那就再次createBean
        // 2.2 BeanNameAware.setBeanName()
        // 2.3 如有 则postProcessBeforeInitialization()
        // 2.4 InitializingBean afterPropertiesSet()
        // 2.5 postProcessAfterInitialization()， 在本例中为AopBeanPostProcessor
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                singletonObjects.put(beanName, createBean(beanName));
            }
        }

    }

    public static List<MethodInterceptor> getMethodInterceptors() {
        return methodInterceptors;
    }


    private Object createBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        Class clazz = beanDefinition.getType();

        try {
            //2.0 无参构造方法
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //2.1 autowired
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true);
                    String fieldName = f.getName();
                    Object bean = getBean(fieldName);
                    f.set(instance, bean);
                }
            }

            //2.2 BeanNameAware
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 2.3 如有 则postProcessBeforeInitialization()
            // postProcessBeforeInitialization

            // 2.4
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            // 2.5 有意思的形容 全公司员工  挨个过安检  看看要不要特殊处理
            for (BeanPostProcessor processor
                    : beanPostProcessors) {

                instance = processor.postProcessAfterInitialization(
                                        instance,
                                        beanName
                                );
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            return null;
        } else {
            if (beanDefinition.getScope().equals("singleton")) {
                try {
                    Object bean = singletonObjects.get(beanName);
                    if (bean == null) {
                        // 这个地方 createBean 是因为有可能User需要自动注入Order的时候 Order还没有办法被全局For循环扫描到
                        // 此时如果想autowired注入 就必须先创建Order的bean 然后再注入到User里面
                        bean = createBean(beanName);
                        singletonObjects.put(beanName, bean);
                    }
                    return bean;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (beanDefinition.getScope().equals("prototype")) {
                try {
                    Object instance = beanDefinition.getType().getDeclaredConstructor().newInstance();
                    return instance;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
}
