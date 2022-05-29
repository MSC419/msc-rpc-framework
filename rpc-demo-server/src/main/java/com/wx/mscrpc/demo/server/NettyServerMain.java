package com.wx.mscrpc.demo.server;

import com.wx.mscrpc.annotation.RpcScan;
import com.wx.mscrpc.transport.netty.server.NettyServer;

/**
 * @Description 用Netty做网络传输的例子服务器端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
@RpcScan
public class NettyServerMain {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer("127.0.0.1",9999);
        nettyServer.start();
    }
}
