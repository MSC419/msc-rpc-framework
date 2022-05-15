package com.wx.mscrpc.utils.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Curator是Apache ZooKeeper的Java / JVM客户端库
 * @Author MSC419
 * @Date 2022/5/7 20:35
 * @Version 1.0
 */
@Slf4j
public class CuratorHelper {
    private static final int SLEEP_MS_BETWEEN_RETRIES = 100;//两次重试之间等待时间
    private static final int MAX_RETRIES = 3;//最大重试次数
    private static final String CONNECT_STRING = "127.0.0.1:2181";//要连接的服务器地址
    private static final int CONNECTION_TIMEOUT_MS = 10 * 1000;//连接超时时间
    private static final int SESSION_TIMEOUT_MS = 60 * 1000;//会话超时时间
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";//根节点路径

    //客户端本地缓存
    //TODO 为什么这就算客户端本地缓存了？
    //key：服务名称serviceName，value：String类型的host:port，这里用List可能是为了以后多个主机提供同一个服务做扩展
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> PERSISTENT_REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final Set<String> EPHEMERAL_REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();

    /**
     * @Description     新建zookeeper客户端连接服务器（服务器地址为"127.0.0.1:2181"）
     * 注意这里的客户端和rpc的客户端服务端不一样，rpc里的客户端服务端都是作为zookeeper的客户端去连接zookeeper
     * 服务器，创建节点与获取节点数据的
     * @param
     * @Return
     * @Author MSC419
     * @Date 2022/5/15 16:25
     */
    public static CuratorFramework getZkClient(){
        // 重试策略，重试3次，并在两次重试之间等待100毫秒，以防出现连接问题。
        RetryPolicy retryPolicy = new RetryNTimes(
                MAX_RETRIES, SLEEP_MS_BETWEEN_RETRIES);

        return CuratorFrameworkFactory.builder()
                //要连接的服务器(可以是服务器列表)
                .connectString(CONNECT_STRING)
                .retryPolicy(retryPolicy)
                //连接超时时间，10秒
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                //会话超时时间，60秒
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .build();
    }

    /**
     * @Description 根据节点路径创建临时节点 临时节点存储在zookeeper中，当断开连接时被删除
     * @param zkClient  与zookeeper服务器连接的zookeeper客户端
     * @param path      节点路径
     * @Author MSC419
     * @Date 2022/5/8 11:09
     */
    public static void createEphemeralNode(final CuratorFramework zkClient, final String path) {
        try {
            // 临时节点已存在
            if (EPHEMERAL_REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("临时节点已经存在,临时节点是:[{}]", path);
            } else {
                // 临时节点不存在,则创建临时节点
                //eg: /MyRPC/com.whc.rpc.api.UserService/127.0.0.1:9000
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                log.info("临时节点成功被创建,临时节点是:[{}]", path);
            }
            EPHEMERAL_REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("创建临时节点失败[{}]", path);
        }
    }

    // 创建服务名永久节点PERSISTENT
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            // 永久节点已存在
            if (PERSISTENT_REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("永久节点已经存在,永久节点是:[{}]", path);
            } else {
                // 永久节点不存在,则创建永久节点
                //eg: /MyRPC/com.whc.rpc.api.UserService
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("永久节点成功被创建,永久节点是:[{}]", path);
            }
            PERSISTENT_REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("创建永久节点失败[{}]", path);
        }
    }

    /**
     * @Description         根据服务名获取节点中的内容
     * @param zkClient      与zookeeper服务器连接的zookeeper客户端
     * @param serviceName   服务名称
     * @return TODO 我不明白这里为啥要返回List<String>，是为了以后多个主机提供同一个服务做扩展？
     */
    public static List<String> getChildrenNodes(final CuratorFramework zkClient, final String serviceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(serviceName)) {
            return SERVICE_ADDRESS_MAP.get(serviceName);
        }
        List<String> result = Collections.emptyList();//返回了一个空的List
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, result);
            registerWatcher(zkClient, serviceName);
        } catch (Exception e) {
            log.error("getChildrenNodes occur exception:", e);
        }
        return result;
    }

    /**
     * @Description         对节点注册监听
     * @param zkClient
     * @param serviceName
     * @Author MSC419
     * @Date 2022/5/15 21:18
     */
    private static void registerWatcher(CuratorFramework zkClient, String serviceName) throws Exception {
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        // 1. 创建监听对象
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);


        // 2. 绑定监听器
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                // 重新获取节点的孩子节点, 即重新获取服务列表信息
                List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
                // 更新客户端本地服务缓存
                SERVICE_ADDRESS_MAP.put( serviceName, serviceAddresses);
                log.info("服务地址列表:{}", SERVICE_ADDRESS_MAP.get(serviceName));
            }
        });

        // 3. 开启
        pathChildrenCache.start();
    }


}
