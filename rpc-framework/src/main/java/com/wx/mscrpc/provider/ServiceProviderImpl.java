package com.wx.mscrpc.provider;

import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.exception.RpcException;
import com.wx.mscrpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/4/3 19:04
 * @Version 1.2
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    /**
     * 接口名和服务的对应关系
     * key:service/interface name
     * value:service
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void addServiceProvider(T service) {
        //这一句话的意思就是：service的包含路径的类名，即：com.wx.msc.HelloServiceImpl
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
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(null == service){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }
}
