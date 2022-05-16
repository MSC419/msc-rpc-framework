package com.wx.mscrpc.loadbalancer;

import java.util.List;

/**
 * @Description 负载均衡接口。给服务器地址列表,根据不同的负载均衡策略选择一个
 * @Author MSC419
 * @Date 2022/5/16 20:02
 * @Version 1.0
 */
public interface LoadBalancer {
    String balance(List<String> addressList);
}
