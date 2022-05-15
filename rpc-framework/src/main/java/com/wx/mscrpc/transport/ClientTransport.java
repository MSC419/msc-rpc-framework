package com.wx.mscrpc.transport;

import com.wx.mscrpc.dto.RpcRequest;

/**
 * @Description 向服务端发送RpcRequest
 * @Author MSC419
 * @Date 2022/4/4 16:54
 * @Version 2.0
 */
public interface ClientTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
