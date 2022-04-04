package com.wx.mscrpc.registry;

/**
 * @Description 注册服务接口
 * @Author MSC419
 * @Date 2022/4/3 19:04
 * @Version 1.2
 */
public interface ServiceRegistry {
    <T> void register(T server);

    Object getService(String serverName);

}
