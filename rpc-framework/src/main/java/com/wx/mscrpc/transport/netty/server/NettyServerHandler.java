package com.wx.mscrpc.transport.netty.server;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.handler.RpcRequestHandler;
import com.wx.mscrpc.utils.concurrent.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //TODO 这一坨为什么要用static final 修饰？
    private static final String THREAD_NAME_PREFIX = "netty-server-handler-rpc-pool";
    private static final RpcRequestHandler rpcRequestHandler;
    private static final ExecutorService threadPool;

    static {
        rpcRequestHandler = new RpcRequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        threadPool.execute(() -> {
            log.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
            try {
                log.info(String.format("server receive msg: %s", msg));
                RpcRequest rpcRequest = (RpcRequest) msg;
                //执行目标方法（客户端需要执行的方法）并且返回方法结果
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info(String.format("server get result: %s", result.toString()));
                //返回方法执行结果给客户端
                ChannelFuture f = ctx.writeAndFlush(RpcResponse.success(result,rpcRequest.getRequestId()));
                f.addListener(ChannelFutureListener.CLOSE);
            } finally {
                //确保 ByteBuf 被释放，不然可能会有内存泄露问题
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception:"+cause);
        cause.printStackTrace();
        ctx.close();
    }
}
