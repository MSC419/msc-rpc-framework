package com.wx.mscrpc.transport.netty;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.serializer.Serializer;
import com.wx.mscrpc.serializer.kryo.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 利用Netty做网络传输的服务端
 * @Author MSC419
 * @Date 2022/4/4 17:09
 * @Version 1.0
 */
@Data
@Slf4j
public class NettyRpcServer {
    private int port;
    private KryoSerializer kryoSerializer;

    public NettyRpcServer(int port) {
        this.port = port;
        kryoSerializer = new KryoSerializer();
    }

    public void run(){
        //创建两个线程组 boosGroup、workerGroup
        //Boss线程专门用于接收来自客户端的连接。一般只开启一条线程，除非一个Nett有服务同时监听多个窗口
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程用于处理已经被Boss线程接收的连接。线程数默认是CPU核数的两倍
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();//创建服务启动辅助类ServerBootstrap，并配置启动参数
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    //TCP Socket为NioServerSocketChannel，UDP为DatagramChannel
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //给workerGroup的EventLoop对应的管道设置处理器
                            //给pipeline管道设置处理器
                            ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    // 设置tcp缓冲区
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //阻塞主线程，直到Socket通达被关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            //关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
