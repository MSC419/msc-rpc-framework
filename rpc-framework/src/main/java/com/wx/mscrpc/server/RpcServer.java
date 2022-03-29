package com.wx.mscrpc.server;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description 服务端类
 * @Author MSC419
 * @Date 2022/3/29 21:06
 * @Version 1.0
 */
@Slf4j
public class RpcServer {

    private ExecutorService threadPool;

    public RpcServer() {
        // 线程池参数
        int corePoolSize = 10;//保留在线程池中的核心线程数（即使他们是空闲的）
        int maximumPoolSizeSize = 100;//池中允许的最大线程数
        long keepAliveTime = 1;//当线程数大于核心时，多余的空闲线程在终止前等待新任务的最长时间。
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);//线程池的任务队列
        ThreadFactory threadFactory = Executors.defaultThreadFactory();//新建一个线程工厂

        //创建线程池
        this.threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSizeSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }
    /**
     * @Description 注册服务
     * @param service 要注册的服务对象
     * @param port 该服务对应的端口
     * @Return 无
     * @Author MSC419
     * @Date 2022/3/29 21:46
     */
    public void register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("server starts...");
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                log.info("client connected");
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
}
