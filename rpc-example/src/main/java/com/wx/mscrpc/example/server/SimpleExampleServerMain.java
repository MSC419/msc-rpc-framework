//package com.wx.mscrpc.example.server;
//
//import com.wx.mscrpc.api.HelloService;
//import com.wx.mscrpc.provider.ServiceProviderImpl;
//import com.wx.mscrpc.transport.socket.SocketRpcServer;
//
///**
// * @Description
// * @Author MSC419
// * @Date 2022/3/29 22:48
// * @Version 1.0
// */
//public class SimpleExampleServerMain {
//    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        SocketRpcServer socketRpcServer = new SocketRpcServer("127.0.0.1", 8080);
//        socketRpcServer.publishService(helloService,HelloService.class);
//    }
//}
