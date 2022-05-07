package com.wx.mscrpc.proxy;


import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.transport.ClientTransport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

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
    /*
     * 用于发送请求给服务端，对应socket和netty两种实现方式
     * TODO 这里为什么要用final修饰？
     */
    private final ClientTransport clientTransport;


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
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();

        return clientTransport.sendRpcRequest(rpcRequest);
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
