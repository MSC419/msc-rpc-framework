package com.wx.mscrpc.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/30 0:20
 * @Version 1.0
 */
@Slf4j
class ExtensionLoaderTest {
    public static void main(String[] args) {

        String fileName = "META-INF/extensions/com.wx.mscrpc.loadbalancer.LoadBalancer";
//        String fileName = "t1/com.wx.mscrpc.loadbalancer.LoadBalancer";
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoaderTest.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    System.out.println(resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}