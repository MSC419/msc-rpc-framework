package com.wx.mscrpc.transport.netty.client;

import com.wx.mscrpc.dto.RpcMessage;
import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.enumeration.SerializerCode;
import com.wx.mscrpc.extension.ExtensionLoader;
import com.wx.mscrpc.loadbalancer.LoadBalancer;
import com.wx.mscrpc.loadbalancer.RandomLoadBalance;
import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.registry.ZkServiceRegistry;
import com.wx.mscrpc.transport.ClientTransport;
import com.wx.mscrpc.utils.checker.RpcMessageChecker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/6 11:27
 * @Version 1.0
 */
@Slf4j
public class NettyClientClientTransport implements ClientTransport {

    private ServiceRegistry serviceRegistry;
    public NettyClientClientTransport() {
        this.serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    public NettyClientClientTransport(LoadBalancer loadBalancer) {
        this.serviceRegistry = new ZkServiceRegistry(loadBalancer);
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            //从注册中心取到服务地址
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            //监听该服务地址
            Channel channel = ChannelProvider.get(inetSocketAddress);
            //将rpcRequest包装成RpcMessage发送给该服务地址，并得到该地址返回的rpcResponse，将rpcResponse返回
            if (channel.isActive()) {
                RpcMessage rpcMessage = buildRpcMessage(rpcRequest);
                channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        log.info("client send message: {}", rpcMessage);
                    } else {
                        future.channel().close();
                        log.error("Send failed:", future.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                log.info("client get rpcResponse from channel:{}", rpcResponse);
                //校验 RpcResponse 和 RpcRequest
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                result.set(rpcResponse.getData());
            } else {
                System.exit(0);
            }

        } catch (InterruptedException e) {
            log.error("occur exception when send rpc message from client:", e);
        }

        return result.get();
    }

    /**
     * @Description 根据RpcRequest构建RPC通用信息结构
     * @Author MSC419
     * @Date 2022/5/23 10:08
     */
    private RpcMessage buildRpcMessage(RpcRequest rpcRequest) {

        return RpcMessage.builder()
                .messageType(PackageType.REQUEST_PACK.getCode())
                .serializeType(SerializerCode.KRYO.getCode())
                //.requestId(rpcRequest.getRequestId())
                .data(rpcRequest)
                .build();
    }
}

