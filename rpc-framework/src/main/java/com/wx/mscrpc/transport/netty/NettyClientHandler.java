package com.wx.mscrpc.transport.netty;

import com.wx.mscrpc.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 客户端业务处理Handler，实现对发送到服务端消息的处理逻辑
 * @Author MSC419
 * @Date 2022/4/4 17:10
 * @Version 2.0
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    //接收服务端发送过来的消息
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcResponse rpcResponse = (RpcResponse) msg;
            log.info(String.format("client receive msg: %s", rpcResponse));
            // 声明一个 AttributeKey 对象
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            // 将服务端的返回结果保存到 AttributeMap 上，AttributeMap 可以看作是一个Channel的共享数据源
            // AttributeMap的key是AttributeKey，value是Attribute
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("client catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
