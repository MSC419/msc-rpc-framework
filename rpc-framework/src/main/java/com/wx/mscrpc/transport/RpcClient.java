package com.wx.mscrpc.transport;

import com.wx.mscrpc.dto.RpcRequest;

/**
 * @Description RpcClient接口，SocketRpcClient和NettyRpcClient实现接口RpcClient
 * 在调用RpcClientProxy时就能统一写法
 * @Author MSC419
 * @Date 2022/4/4 16:54
 * @Version 2.0
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
