//package com.wx.mscrpc.transport.socket;
//
//
//import com.wx.mscrpc.dto.RpcRequest;
//import com.wx.mscrpc.dto.RpcResponse;
//import com.wx.mscrpc.exception.RpcException;
//import com.wx.mscrpc.registry.ServiceRegistry;
//import com.wx.mscrpc.registry.ZkServiceRegistry;
//import com.wx.mscrpc.transport.ClientTransport;
//import com.wx.mscrpc.utils.checker.RpcMessageChecker;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
///**
// * @Description 客户端类：通过Socket发送请求
// * @Author MSC419
// * @Date 2022/3/29 21:08
// * @Version 1.0
// */
//@Slf4j
//public class SocketRpcClient implements ClientTransport {
//
//    private final ServiceRegistry serviceRegistry;
//
//    public SocketRpcClient() {
//        this.serviceRegistry = new ZkServiceRegistry();
//    }
//
//    /**
//     * @Description 发送请求服务的请求
//     * @param rpcRequest  请求描述
//     * @Return 这个返回值暂时不知道有什么用
//     * @Author MSC419
//     * @Date 2022/3/29 21:39
//     */
//    @Override
//    public Object sendRpcRequest(RpcRequest rpcRequest){
//
//        InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
//        try (Socket socket = new Socket()) {
//            socket.connect(inetSocketAddress);
//
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            //通过输出流发送数据到服务端
//            objectOutputStream.writeObject(rpcRequest);
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            //从输入流中读取RpcResponse
//            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
//            //校验
//            RpcMessageChecker.check(rpcRequest, rpcResponse);
//
//            return rpcResponse.getData();
//
//        } catch (IOException | ClassNotFoundException e) {
//            log.error("occur exception when send sendRpcRequest");
//            throw new RpcException("调用服务失败：", e);
//        }
//    }
//
//}
