package com.wx.mscrpc.example.client;

import com.wx.mscrpc.example.api.Hello;
import com.wx.mscrpc.example.api.HelloService;
import com.wx.mscrpc.transport.ClientTransport;
import com.wx.mscrpc.proxy.RpcClientProxy;
import com.wx.mscrpc.transport.netty.client.NettyClientClientTransport;

import java.net.InetSocketAddress;

/**
 * @Description 用Netty做网络传输的例子客户端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
public class NettyClient {
    public static void main(String[] args) {
        ClientTransport clientTransport =new NettyClientClientTransport(new InetSocketAddress("127.0.0.1", 1234));
        RpcClientProxy rpcClientProxy = new RpcClientProxy(clientTransport);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("uam","111"));
//        System.out.println(hello);
        assert "Hello description is 111".equals(hello);
    }
}
