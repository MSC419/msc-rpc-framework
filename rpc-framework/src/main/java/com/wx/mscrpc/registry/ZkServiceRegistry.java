package com.wx.mscrpc.registry;

import com.wx.mscrpc.loadbalancer.LoadBalancer;
import com.wx.mscrpc.loadbalancer.RandomLoadBalance;
import com.wx.mscrpc.utils.zookeeper.CuratorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description 基于zookeeper实现注册中心
 * @Author MSC419
 * @Date 2022/5/8 9:51
 * @Version 3.0
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry{
    private final CuratorFramework zkClient;
    private final LoadBalancer loadBalancer;

    public ZkServiceRegistry() {
        zkClient = CuratorHelper.getZkClient();//新建zookeeper客户端
        zkClient.start();//zookeeper客户端与zookeeper服务器建立连接
        loadBalancer = new RandomLoadBalance();
    }

    public ZkServiceRegistry(LoadBalancer loadBalancer) {
        zkClient = CuratorHelper.getZkClient();
        zkClient.start();

        if(loadBalancer == null) {
            this.loadBalancer = new RandomLoadBalance();
        } else {
            this.loadBalancer = loadBalancer;
        }
    }


    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        //  /my-rpc/{serviceName}/{host}:{port}
        //根节点下注册子节点：服务
        StringBuilder servicePath = new StringBuilder(CuratorHelper.ZK_REGISTER_ROOT_PATH).append("/").append(serviceName);
        //创建永久节点：/my-rpc/{serviceName}
        CuratorHelper.createPersistentNode(zkClient, servicePath.toString());
        //服务子节点下注册子节点：服务地址
        servicePath.append(inetSocketAddress.toString());
        //创建临时节点：/my-rpc/{serviceName}/{host}:{port}
        CuratorHelper.createEphemeralNode(zkClient, servicePath.toString());
        log.info("节点创建成功，节点为:{}", servicePath);
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<String> serviceAddress = CuratorHelper.getChildrenNodes(zkClient, serviceName);
            // 负载均衡
            String address = loadBalancer.balance(serviceAddress);
            log.info("根据负载均衡策略后, 返回的服务器ip地址为:" + address);
            return new InetSocketAddress(address.split(":")[0], Integer.parseInt(address.split(":")[1]));
        } catch (Exception e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}
