package com.wx.mscrpc.registry;

import com.wx.mscrpc.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @Description 服务提供方的服务注册中心接口
 * @Author MSC419
 * @Date 2022/4/3 19:04
 * @Version 1.2
 */
@SPI
public interface ServiceRegistry {
    /**
     * @Description                 注册服务
     * @param serviceName           服务名称
     * @param inetSocketAddress     提供服务的地址
     * @Author MSC419
     * @Date 2022/5/8 11:13
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * @Description                 查找服务
     * @param serviceName           服务名称
     * @Return                      提供服务的地址
     * @Author MSC419
     * @Date 2022/5/8 11:14
     */
    InetSocketAddress lookupService(String serviceName);
}
