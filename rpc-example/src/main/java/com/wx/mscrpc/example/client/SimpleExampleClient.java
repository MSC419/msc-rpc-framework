package com.wx.mscrpc.example.client;

import com.wx.mscrpc.example.api.Hello;
import com.wx.mscrpc.example.api.HelloService;
import com.wx.mscrpc.transport.ClientTransport;
import com.wx.mscrpc.proxy.RpcClientProxy;
import com.wx.mscrpc.transport.socket.SocketClientTransport;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleClient {
    public static void main(String[] args) {
        ClientTransport clientTransport = new SocketClientTransport("127.0.0.1",1234);
        RpcClientProxy clientProxy = new RpcClientProxy(clientTransport);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("uam","111"));
        System.out.println(hello);
    }
}
