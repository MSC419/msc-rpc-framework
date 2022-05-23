package com.wx.mscrpc.transport;

import com.wx.mscrpc.annotation.RpcScan;
import com.wx.mscrpc.annotation.RpcService;
import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.exception.RpcException;
import com.wx.mscrpc.provider.ServiceProvider;
import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.utils.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @Description 扫描服务并注册
 * @Author MSC419
 * @Date 2022/5/23 20:02
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer{
    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    public void scanServices() {
        //获取服务端main方法的类名
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(RpcScan.class)) {
                log.error("启动类缺少 @RpcScan 注解");
                throw new RpcException(RpcErrorMessageEnum.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RpcException(RpcErrorMessageEnum.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(RpcScan.class).value();
        //如果RpcScan没有指定value（即basePackage为空），那么把启动类的包名赋给它
        if("".equals(basePackage)) {
            //basePackage:com.wx.mscrpc.example.server
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        log.info("basePackage:{}",basePackage);
        //扫描basePackage及其子包下所有的类，并将其 Class 对象放入一个 Set 中返回。
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            //如果该类有注解@RpcService，说明是个服务类
            if(clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).name();
                Object obj;
                try {
                    //实例化该服务类
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                //注册服务
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        publishService(obj, oneInterface);
                    }
                } else {
                    publishService(obj, clazz);
                }
            }
        }
    }


    public <T> void publishService(Object service, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(service);
        //注册服务
        serviceRegistry.registerService(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));

    }
}
