package com.wx.mscrpc.transport.netty;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.serializer.kryo.KryoSerializer;
import com.wx.mscrpc.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 利用Netty做网络传输的客户端
 * @Author MSC419
 * @Date 2022/4/4 17:09
 * @Version 2.0
 */
@Data
@AllArgsConstructor
@Slf4j
public class NettyRpcClient implements RpcClient {
    private String host;
    private int port;
    private static final Bootstrap bootstrap;//线程组

    //TODO 1.为啥要把这一坨放在static里？
    //初始化相关资源比如 EventLoopGroup、Bootstrap
    static{
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
//        try {
            KryoSerializer kryoSerializer = new KryoSerializer();

            //创建bootstrap对象，配置参数
            bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //设置保持活动连接状态
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            /*自定义序列化编解码器*/
                            // RpcResponse -> ByteBuf
                            ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                            // ByteBuf -> RpcRequest
                            ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
//        }finally {
//            eventExecutors.shutdownGracefully();
//        }

    }


    /**
     * @Description 发送RPC请求
     * @param rpcRequest 请求
     * @Return
     * @Author MSC419
     * @Date 2022/4/4 18:33
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {

        try {
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            log.info("client connect  {}", host + ":" + port);
            Channel futureChannel = channelFuture.channel();
            if (futureChannel != null) {
                futureChannel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info(String.format("client send message: %s", rpcRequest.toString()));
                    } else {
                        log.error("Send failed:", future.cause());
                    }
                });
                //对通道关闭进行监听
                futureChannel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = futureChannel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }

        return null;
    }
}
