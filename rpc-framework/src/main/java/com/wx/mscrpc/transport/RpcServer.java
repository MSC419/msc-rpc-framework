package com.wx.mscrpc.transport;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/23 20:06
 * @Version 1.0
 */
public interface RpcServer {
    //启动连接
    void start();
    //注册服务
    <T> void publishService(Object service, Class<T> serviceClass);
}
