package com.wx.mscrpc.test;

import com.wx.mscrpc.example.api.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/4/3 21:51
 * @Version 1.0
 */
public class MyClient {

    public static void main(String[] args){
        HelloService helloService;
        //HelloService.class：com.wx.mscrpc.example.api.HelloService
        System.out.println(HelloService.class);
    }
}
