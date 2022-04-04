package com.wx.mscrpc.transport;


import com.wx.mscrpc.dto.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description 调用远程服务的代理类
 * @Author MSC419
 * @Date 2022/3/29 21:17
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private RpcClient rpcClient;


    /**
     * @Description 代理调用方法
     * @param proxy 代理实例
     * @param method 要调用的方法
     * @param args 调用方法需要传入的参数
     * @Return
     * @Author MSC419
     * @Date 2022/3/29 22:34
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Call invoke method and invoked method: {}", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();

        return rpcClient.sendRpcRequest(rpcRequest);
    }

    /**
     * @Description 得到接口的代理对象
     * @param clazz 接口类
     * @Return clazz子类的对象
     * @Author MSC419
     * @Date 2022/3/29 22:45
     */
    public <T>T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }


}
