package com.wx.mscrpc.transport.netty.server;

import com.wx.mscrpc.dto.RpcMessage;
import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.handler.RpcRequestHandler;
import com.wx.mscrpc.utils.concurrent.ThreadPoolFactory;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @Description 服务端业务处理Handler，实现对接收客户端消息的处理逻辑
 * @Author MSC419
 * @Date 2022/4/4 17:10
 * @Version 2.0
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    //TODO 这一坨为什么要用static final 修饰？
    private static final String THREAD_NAME_PREFIX = "netty-server-handler-rpc-pool";
    private static final RpcRequestHandler rpcRequestHandler;
    private static final ExecutorService threadPool;

    static {
        rpcRequestHandler = new RpcRequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        threadPool.execute(() -> {
            log.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
            try {
                // 不理心跳消息
                if (msg.getMessageType() == PackageType.HEARTBEAT_PACK.getCode()) {
                    return;
                }else if(msg.getMessageType() == PackageType.REQUEST_PACK.getCode()){
                    log.info(String.format("server receive msg: %s", msg));
                    RpcRequest rpcRequest = (RpcRequest) msg.getData();
                    //执行目标方法（客户端需要执行的方法）并且返回方法结果
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    log.info(String.format("server get result: %s", result.toString()));
                    //执行结果先封装成RpcResponse再封装成RpcMessage返回给客户端
                    RpcMessage rpcResponseMessage = RpcMessage.builder()
                            .messageType(PackageType.RESPONSE_PACK.getCode())
                            .serializeType(msg.getSerializeType())
                            .data(RpcResponse.success(result,rpcRequest.getRequestId()))
                            .build();
                    ChannelFuture f = ctx.writeAndFlush(rpcResponseMessage);
                    f.addListener(ChannelFutureListener.CLOSE);
                }

            } finally {
                //确保 ByteBuf 被释放，不然可能会有内存泄露问题
                ReferenceCountUtil.release(msg);
            }
        });
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 处理空闲状态的
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception", cause);
        ctx.close();
    }
}
