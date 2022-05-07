package com.wx.mscrpc.registry;

import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description 默认的服务注册中心实现，通过 Map 保存服务信息，可以通过 zookeeper 来改进
 * @Author MSC419
 * @Date 2022/4/3 19:04
 * @Version 1.2
 */
@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry{
    /**
     * 接口名和服务的对应关系，TODO 处理一个接口被两个实现类实现的情况
     * key:service/interface name
     * value:service
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * @Description 注册服务
     * 将这个服务所有实现的接口都注册
     * @param service
     * @Return
     * @Author MSC419
     * @Date 2022/4/3 19:23
     */
    @Override
    public synchronized <T> void register(T service) {
        //这一句话的意思就是：service的包含路径的类名，即：github.javaguide.HelloServiceImpl
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);

        Class[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class i : interfaces){
            serviceMap.put(i.getCanonicalName(), service);
        }
        log.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(null == service){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
