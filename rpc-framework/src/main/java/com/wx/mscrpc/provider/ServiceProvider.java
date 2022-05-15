package com.wx.mscrpc.provider;

/**
 * @Description 保存和提供服务实例对象。服务端使用。
 * @Author MSC419
 * @Date 2022/5/7 20:37
 * @Version 1.0
 */
public interface ServiceProvider {
    /**
     * 保存服务提供者
     */
    <T> void addServiceProvider(T service);

    /**
     * 获取服务提供者
     */
    Object getServiceProvider(String serviceName);
}
