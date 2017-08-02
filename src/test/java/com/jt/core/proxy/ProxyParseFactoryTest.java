package com.jt.core.proxy;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Enumeration;

import static org.junit.Assert.*;

public class ProxyParseFactoryTest {

    @Test
    public void test01() throws Exception {
        String basePackage = "/com.jt.core.proxy";
        basePackage = basePackage.replaceAll(".", "/");
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(basePackage);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String file = url.getFile();
            File f = new File(file);
            if (f.exists()) {
                File[] classes = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith("class");
                    }
                });
                for (File c : classes) {
                    System.out.println(c);
                }
            }
        }

    }

    @Test
    public void test02() throws Exception {
        URL resource = ClassLoader.getSystemClassLoader().getResource("");
        System.out.println(resource);
    }

}