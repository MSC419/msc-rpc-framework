//package com.wx.mscrpc.transport.socket;
//
//import com.wx.mscrpc.dto.RpcRequest;
//import com.wx.mscrpc.dto.RpcResponse;
//import com.wx.mscrpc.handler.RpcRequestHandler;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
///**
// * @Description 处理客户端请求的线程
// * @Author MSC419
// * @Date 2022/3/29 21:52
// * @Version 1.1
// */
//@Data
//@Slf4j
//public class SocketRpcRequestHandlerRunnable implements Runnable{
//    private Socket socket;
//    private static final RpcRequestHandler rpcRequestHandler;
//    static {
//        rpcRequestHandler=new RpcRequestHandler();
//    }
//    public SocketRpcRequestHandlerRunnable(Socket socket) {
//        this.socket = socket;
//    }
//
//
//    @Override
//    public void run() {
//        log.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
//        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
//            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
//
//            Object result = rpcRequestHandler.handle(rpcRequest);
//            objectOutputStream.writeObject(RpcResponse.success(result,rpcRequest.getRequestId()));
//            objectOutputStream.flush();
//        } catch (IOException | ClassNotFoundException e) {
//            log.error("occur exception:", e);
//        }
//    }
//
//
//}
