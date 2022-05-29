package com.wx.mscrpc.demo.client;

import com.wx.mscrpc.api.Hello;
import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.api.User;
import com.wx.mscrpc.api.UserService;
import com.wx.mscrpc.loadbalancer.RandomLoadBalance;
import com.wx.mscrpc.proxy.RpcClientProxy;
import com.wx.mscrpc.transport.ClientTransport;
import com.wx.mscrpc.transport.netty.client.NettyClientClientTransport;

/**
 * @Description 用Netty做网络传输的例子客户端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
public class NettyClientMain {
    public static void main(String[] args) {
        ClientTransport clientTransport =new NettyClientClientTransport(new RandomLoadBalance());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(clientTransport);

        //调用服务1
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("uam","how are you?"));
        System.out.println(hello);

        //调用服务2
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        User userByUserId = userService.getUserByUserId(10);
        System.out.println(userByUserId);
        User user = User.builder().userName("张三").id(100).sex(true).build();
        Integer integer = userService.insertUserId(user);
        System.out.println("向服务端插入数据:" + integer);


    }
}
