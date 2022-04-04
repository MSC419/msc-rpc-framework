package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.transport.RpcClient;
import com.wx.mscrpc.transport.RpcClientProxy;
import com.wx.mscrpc.transport.netty.NettyRpcClient;

/**
 * @Description 用Netty做网络传输的例子客户端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
public class NettyClient {
    public static void main(String[] args) {
        RpcClient rpcClient=new NettyRpcClient("127.0.0.1", 1234);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.sayHello("Uam");
        System.out.println(hello);
    }
}
