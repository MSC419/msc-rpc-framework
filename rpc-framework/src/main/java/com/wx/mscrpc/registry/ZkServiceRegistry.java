package com.wx.mscrpc.registry;

import com.wx.mscrpc.utils.zookeeper.CuratorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @Description 基于zookeeper实现注册中心
 * @Author MSC419
 * @Date 2022/5/8 9:51
 * @Version 3.0
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry{
    private final CuratorFramework zkClient;
    public ZkServiceRegistry() {
        zkClient = CuratorHelper.getZkClient();//新建zookeeper客户端
        zkClient.start();//zookeeper客户端与zookeeper服务器建立连接
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
        String serviceAddress = CuratorHelper.getChildrenNodes(zkClient, serviceName).get(0);
        log.info("成功找到服务地址:{}", serviceAddress);
        //InetSocketAddress( host , port )
        return new InetSocketAddress(serviceAddress.split(":")[0], Integer.parseInt(serviceAddress.split(":")[1]));
    }

}
