package com.wx.mscrpc.transport.socket;

import com.wx.mscrpc.registry.ServiceRegistry;
import com.wx.mscrpc.transport.RpcRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description 服务端类：通过Socket接受与发送数据
 * @Author MSC419
 * @Date 2022/3/29 21:06
 * @Version 1.0
 */
@Slf4j
public class SocketRpcServer {
    //线程池参数
    private static final int CORE_POOL_SIZE = 10;//保留在线程池中的核心线程数（即使他们是空闲的）
    private static final int MAXIMUM_POOL_SIZE_SIZE = 100;//池中允许的最大线程数
    private static final int KEEP_ALIVE_TIME = 1;//当线程数大于核心时，多余的空闲线程在终止前等待新任务的最长时间。
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private ExecutorService threadPool;
    private RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
    private final ServiceRegistry serviceRegistry;

    public SocketRpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * @Description 开启Socket连接
     * @param port
     * @Return
     * @Author MSC419
     * @Date 2022/4/3 20:25
     */
    public void start( int port){

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("server starts...");
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                log.info("client connected");
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket, rpcRequestHandler, serviceRegistry));
            }
            threadPool.shutdown();//关闭线程池
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }

}
