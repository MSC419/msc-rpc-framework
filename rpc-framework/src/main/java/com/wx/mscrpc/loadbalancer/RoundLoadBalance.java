package com.wx.mscrpc.loadbalancer;

import java.util.List;

/**
 * @Description 轮询实现负载均衡
 * @Author MSC419
 * @Date 2022/5/16 20:09
 * @Version 1.0
 */
public class RoundLoadBalance implements LoadBalancer{
    private int index = 0;
    @Override
    public String balance(List<String> addressList) {
        if(index>=addressList.size()){
            index %= addressList.size();
        }
        return addressList.get(index++);
    }
}
