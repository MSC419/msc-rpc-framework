package com.wx.mscrpc.transport.netty.client;

import com.wx.mscrpc.serializer.KryoSerializer;
import com.wx.mscrpc.transport.netty.codec.NettyDecoder;
import com.wx.mscrpc.transport.netty.codec.NettyEncoder;
import com.wx.mscrpc.transport.netty.codec.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Description 用于初始化 和 关闭 Bootstrap 对象
 * @Author MSC419
 * @Date 2022/5/6 11:30
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor
public class NettyClient {
    private static final Bootstrap bootstrap;//线程组
    private static final EventLoopGroup eventLoopGroup;

    // 初始化相关资源比如 EventLoopGroup、Bootstrap
    //TODO 为啥要把这一坨放在static里？
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 设定 IdleStateHandler 心跳检测每 5 秒进行一次写检测
                        // write()方法超过 5 秒没调用，就调用 userEventTrigger
                        ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        /*自定义序列化编解码器*/
                        // RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new NettyDecoder());
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new NettyEncoder());
                        ch.pipeline().addLast(new NettyClientHandler());

                    }
                });
    }


    public static void close() {
        log.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }

    public static Bootstrap initializeBootstrap() {
        return bootstrap;
    }
}
