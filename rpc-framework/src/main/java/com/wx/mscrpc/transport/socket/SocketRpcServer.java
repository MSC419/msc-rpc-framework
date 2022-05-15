//package com.wx.mscrpc.transport.socket;
//
//import com.wx.mscrpc.provider.ServiceProvider;
//import com.wx.mscrpc.provider.ServiceProviderImpl;
//import com.wx.mscrpc.registry.ServiceRegistry;
//import com.wx.mscrpc.registry.ZkServiceRegistry;
//import com.wx.mscrpc.utils.concurrent.ThreadPoolFactory;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.concurrent.*;
//
///**
// * @Description 服务端类：通过Socket接受与发送数据
// * @Author MSC419
// * @Date 2022/3/29 21:06
// * @Version 1.0
// */
//@Slf4j
//public class SocketRpcServer {
//
//    private final ExecutorService threadPool;
//    private final String host;
//    private final int port;
//    private final ServiceRegistry serviceRegistry;
//    private final ServiceProvider serviceProvider;
//
//    public SocketRpcServer(String host, int port) {
//        this.host = host;
//        this.port = port;
//        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-server-rpc-pool");
//        serviceRegistry = new ZkServiceRegistry();
//        serviceProvider = new ServiceProviderImpl();
//    }
//    /**
//     * @Description 发布服务
//     * @param service
//     * @param serviceClass
//     * @Return
//     * @Author MSC419
//     * @Date 2022/5/8 11:48
//     */
//    public <T> void publishService(Object service, Class<T> serviceClass) {
//        serviceProvider.addServiceProvider(service);
//        serviceRegistry.registerService(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
//        start();
//    }
//
//    /**
//     * @Description 开启Socket连接
//     * @Return
//     * @Author MSC419
//     * @Date 2022/4/3 20:25
//     */
//    public void start(){
//
//        try(ServerSocket serverSocket = new ServerSocket(port)) {
//            log.info("server starts...");
//            Socket socket;
//            while((socket=serverSocket.accept())!=null){
//                log.info("client connected");
//                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
//            }
//            threadPool.shutdown();//关闭线程池
//        } catch (IOException e) {
//            log.error("occur IOException:", e);
//        }
//    }
//
//}
