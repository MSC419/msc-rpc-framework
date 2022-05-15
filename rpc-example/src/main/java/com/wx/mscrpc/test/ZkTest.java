package com.wx.mscrpc.test;

import org.apache.zookeeper.*;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/9 9:33
 * @Version 1.0
 */
public class ZkTest{

    //连接地址及端口号
    private static final String SERVER_HOST = "127.0.0.1:2181";

    //会话超时时间
    private static final int SESSION_TIME_OUT = 2000;

    public static void main(String[] args) throws Exception {
        //参数一：服务端地址及端口号
        //参数二：超时时间
        //参数三：监听器
        ZooKeeper zooKeeper = new ZooKeeper(SERVER_HOST, SESSION_TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //获取事件的状态
                Event.KeeperState state = watchedEvent.getState();
                //判断是否是连接事件
                if (Event.KeeperState.SyncConnected == state) {
                    Event.EventType type = watchedEvent.getType();
                    if (Event.EventType.None == type) {
                        System.out.println("zk客户端已连接...");
                    }
                }
            }
        });
        zooKeeper.create("/java", "Hello World".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("新增ZNode成功");
        zooKeeper.close();
    }
}

