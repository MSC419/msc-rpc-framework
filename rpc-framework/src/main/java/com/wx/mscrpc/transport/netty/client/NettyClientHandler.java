package com.wx.mscrpc.transport.netty.client;

import com.wx.mscrpc.dto.RpcMessage;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.PackageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

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
