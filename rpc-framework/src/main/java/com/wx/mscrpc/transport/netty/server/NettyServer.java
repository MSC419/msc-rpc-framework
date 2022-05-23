package com.wx.mscrpc.transport.netty.server;

import com.wx.mscrpc.provider.ServiceProvider;
import com.wx.mscrpc.provider.ServiceProviderImpl;
import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.registry.ZkServiceRegistry;
import com.wx.mscrpc.serializer.KryoSerializer;
import com.wx.mscrpc.transport.AbstractRpcServer;
import com.wx.mscrpc.transport.RpcServer;
import com.wx.mscrpc.transport.netty.codec.NettyDecoder;
import com.wx.mscrpc.transport.netty.codec.NettyEncoder;
import com.wx.mscrpc.transport.netty.codec.Spliter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Description 利用Netty做网络传输的服务端
 * @Author MSC419
 * @Date 2022/4/4 17:09
 * @Version 1.0
 */
@Data
@Slf4j
public class NettyServer extends AbstractRpcServer {
    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public NettyServer(String host, int port) {
            this.host = host;
            this.port = port;
            serviceRegistry = new ZkServiceRegistry();
            serviceProvider = new ServiceProviderImpl();
            scanServices();
        }

        /**
         * @Description         注册服务
         * @param service       服务
         * @param serviceClass  服务类型
         * @Return
         * @Author MSC419
         * @Date 2022/5/15 18:59
         */
        public <T> void publishService(Object service, Class<T> serviceClass) {
            serviceProvider.addServiceProvider(service);
            //注册服务
            serviceRegistry.registerService(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));

        }

        /**
         * @Description     启动Netty服务端
         * @Author MSC419
         * @Date 2022/5/15 18:58
         */
        public void start(){
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
                            // 30 秒之内没有收到客户端请求的话就关闭连接
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new NettyDecoder());
                            ch.pipeline().addLast(new Spliter());
                            ch.pipeline().addLast(new NettyEncoder());
                            ch.pipeline().addLast(new NettyServerHandler());

                        }
                    })
                    // 设置tcp缓冲区
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 是否开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    ;

            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(host,port).sync();
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
