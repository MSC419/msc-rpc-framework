package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloServer;
import com.wx.mscrpc.client.RpcClientProxy;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleClient {
    public static void main(String[] args) {
        RpcClientProxy clientProxy = new RpcClientProxy("127.0.0.1",1234);
        HelloServer helloServer = clientProxy.getProxy(HelloServer.class);
        String hello = helloServer.sayHello("Uam");
        System.out.println(hello);
    }
}
