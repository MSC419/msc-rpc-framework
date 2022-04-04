package com.wx.mscrpc.transport.socket;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.transport.RpcRequestHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description 处理客户端请求的线程
 * @Author MSC419
 * @Date 2022/3/29 21:52
 * @Version 1.1
 */
@Data
@AllArgsConstructor
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable{
    private Socket socket;
    private RpcRequestHandler rpcRequestHandler;
    private ServiceRegistry serviceRegistry;


    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = rpcRequestHandler.handle(rpcRequest,service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }


}
