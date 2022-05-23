package com.wx.mscrpc.transport.netty.client;

import com.wx.mscrpc.dto.RpcMessage;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.enumeration.SerializerCode;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * @Description 客户端业务处理Handler，实现对发送到服务端消息的处理逻辑
 *
 * @Author MSC419
 * @Date 2022/4/4 17:10
 * @Version 2.0
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        try {
            RpcMessage rpcMessage = (RpcMessage) msg;
            log.info(String.format("client receive msg: %s", rpcMessage));
            if(rpcMessage.getMessageType() == PackageType.RESPONSE_PACK.getCode()){
                RpcResponse rpcResponse = (RpcResponse)rpcMessage.getData();
                // 声明一个 AttributeKey 对象，类似于 Map 中的 key
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse"+rpcResponse.getRequestId());
                // 将服务端的返回结果保存到 AttributeMap 上，AttributeMap 可以看作是一个Channel的共享数据源
                // AttributeMap的key是AttributeKey，value是Attribute
                // 用法：ctx.channel().attr(key).set(value);
                ctx.channel().attr(key).set(rpcResponse);
                ctx.channel().close();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof IdleStateEvent) {
            // 根据上面的配置，超过 5 秒没有写请求，会触发 WRITER_IDLE 事件
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = ctx.channel();
                // 触发写空闲事件后，就应该发心跳了。
                // 组装消息
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setSerializeType(SerializerCode.KRYO.getCode());
                rpcMessage.setMessageType(PackageType.HEARTBEAT_PACK.getCode());
                // 发心跳消息
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    /*
     * 客户端发生异常的时候被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("client catch exception:"+cause);
        cause.printStackTrace();
        ctx.close();
    }
}
