package com.wx.mscrpc.client;


import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.enumeration.RpcResponseCode;
import com.wx.mscrpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description 客户端类：通过Socket发送请求
 * @Author MSC419
 * @Date 2022/3/29 21:08
 * @Version 1.0
 */
@Slf4j
public class RpcClient {
    /**
     * @Description 发送请求服务的请求
     * @param rpcRequest  请求描述
     * @param host  对方IP地址
     * @param port  端口号
     * @Return 这个返回值暂时不知道有什么用
     * @Author MSC419
     * @Date 2022/3/29 21:39
     */

    public Object sendRpcRequest(RpcRequest rpcRequest, String host, int port){

        try(Socket socket = new Socket(host, port)){

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();

            if(rpcResponse == null){
                log.error("调用服务失败,serviceName: {}"+rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
                log.error("调用服务失败,serviceName: {},RpcResponse: {}"+rpcRequest.getInterfaceName(),rpcResponse);
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, "interfaceName:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();

        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败：", e);
        }
    }

}
