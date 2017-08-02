package com.jt.core.loader;


import com.jt.core.annotations.Bind;
import com.jt.core.annotations.Managed;
import com.jt.core.proxy.ProxyParseFactory;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * container, load class
 * Created by he on 2017/8/1.
 * @see Bind
 * @see Managed
 */
public class ContextLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextLoader.class);

    private ContextLoader() {
    }

    private static List<Class> allClass = new LinkedList<>();
    private static Map<String, Object> beanContext = new HashMap<>();

    private static ContextLoader contextLoader = new ContextLoader();

    public <T> T getBean(Class<T> clz) {
        return (T) beanContext.get(clz.getSimpleName());
    }

    public void loadClass(final String basePackage) throws Exception {
        LOGGER.info("开始加载类 {}", basePackage);
        String path = basePackage.replaceAll("\\.", "/");
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);


        File f = new File(resource.getFile());
        if (f.exists()) {
            File[] classes = f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("class");
                }
            });
            for (File c : classes) {
                allClass.add(Class.forName(basePackage + "." + c.getName().substring(0, c.getName().length() - 6)));
            }
        }
        LOGGER.info("所有加载的类 {}", allClass);

        for (Class clz : allClass) {
            boolean annotationPresent = clz.isAnnotationPresent(Managed.class);
            if (annotationPresent) {
                Object o = clz.newInstance();
                beanContext.put(clz.getSimpleName(), o);
            }
        }

        LOGGER.info("管理的实例对象 {}", beanContext);
        for (Class clz : allClass) {

            /*
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                boolean annotationPresent = field.isAnnotationPresent(Bind.class);
                if (annotationPresent) {
                    Object o = beanContext.get(clz.getSimpleName());
                    if (field.getType().equals(Map.class)) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        Type valueType = actualTypeArguments[1];
                        String typeName = valueType.getTypeName();

                        for (Class innerClz : allClass) {
                            if (innerClz.getName().equals(valueType.getTypeName())) {
                                //非接口

                            } else {
                                Type[] genericInterfaces = innerClz.getGenericInterfaces();
                                if (genericInterfaces != null
                                        && genericInterfaces.length > 0
                                        && genericInterfaces[0].getTypeName().equals(valueType.getTypeName())) {
                                    Object o1 = beanContext.get(innerClz.getSimpleName());
                                    //接口

                                }
                            }

                    }
                }
            }
            */

            Method[] declaredMethods = clz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Bind.class)) {
                    Object o = beanContext.get(clz.getSimpleName());

                    Class<?>[] parameterTypes = method.getParameterTypes();

                    Class userDefined = null;
                    for (Class methodClz : parameterTypes) {
                        if (ClassUtils.isPrimitiveOrWrapper(methodClz) || methodClz.equals(String.class)) {
                            continue;
                        }
                        userDefined = methodClz;

                    }

                    if (userDefined != null) {
                        for (Object obj : beanContext.values()) {
                            if (obj.getClass().equals(userDefined)) {
                                //非接口实现

                            } else {
                                //接口实现
                                Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
                                if (genericInterfaces != null
                                        && genericInterfaces.length > 0
                                        && genericInterfaces[0].getTypeName().equals(userDefined.getName())) {
                                    LOGGER.info("注入对象 {} {}", method, obj);
                                    method.invoke(o, obj.getClass().getAnnotation(Managed.class).mapKey(), obj);

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static ContextLoader getInstance() {
        return contextLoader;
    }



    public static void main(String[] args) throws Exception {
        ContextLoader instance = getInstance();
        instance.loadClass("com.jt.core.proxy");
        ProxyParseFactory bean = instance.getBean(ProxyParseFactory.class);
        System.out.println(bean.getFactoryMap());

    }


}
