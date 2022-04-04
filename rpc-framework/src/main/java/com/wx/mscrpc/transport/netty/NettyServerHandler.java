package com.wx.mscrpc.transport.netty;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.registry.DefaultServiceRegistry;
import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.transport.RpcRequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 服务端业务处理Handler，实现对接收客户端消息的处理逻辑
 * @Author MSC419
 * @Date 2022/4/4 17:10
 * @Version 2.0
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
//    private static RpcRequestHandler rpcRequestHandler;
//    private static ServiceRegistry serviceRegistry;
//    static {
//        rpcRequestHandler = new RpcRequestHandler();
//        serviceRegistry = new DefaultServiceRegistry();
//    }
//
//    @Override
//    //获取客户端发来的消息，交给RpcRequestHandler处理，将结果传给客户端
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        try {
//            RpcRequest rpcRequest = (RpcRequest) msg;
//            log.info(String.format("server receive msg: %s", rpcRequest));
//            String interfaceName = rpcRequest.getInterfaceName();
//            Object service = serviceRegistry.getServer(interfaceName);
//            Object result = rpcRequestHandler.handle(rpcRequest, service);
//            log.info(String.format("server get result: %s", result.toString()));
//            //发送消息给客户端
//            //直接这么写行不行？ctx.writeAndFlush(RpcResponse.success(result));不创建ChannelFuture这个
//            ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(result));
//            channelFuture.addListener(ChannelFutureListener.CLOSE);
//        }finally {
//            //手动回收，防止内存泄漏
//            ReferenceCountUtil.release(msg);
//        }
//
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("server catch exception");
//        cause.printStackTrace();
//        ctx.close();
//    }
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RpcRequestHandler rpcRequestHandler;
    private static ServiceRegistry serviceRegistry;
    static {
        rpcRequestHandler=new RpcRequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            logger.info(String.format("server receive msg: %s", rpcRequest));
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = rpcRequestHandler.handle(rpcRequest, service);
            logger.info(String.format("server get result: %s", result.toString()));
            ChannelFuture f = ctx.writeAndFlush(RpcResponse.success(result));
            f.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
