package com.wx.mscrpc.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * @Description 随机实现负载均衡
 * @Author MSC419
 * @Date 2022/5/16 20:08
 * @Version 1.0
 */
public class RandomLoadBalance implements LoadBalancer{
    @Override
    public String balance(List<String> addressList) {
        return addressList.get(new Random().nextInt(addressList.size()));
    }
}
