package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.remoting.socket.RpcClientProxy;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleClient {
    public static void main(String[] args) {
        RpcClientProxy clientProxy = new RpcClientProxy("127.0.0.1",1234);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.sayHello("Uam");
        System.out.println(hello);
    }
}
