package com.wx.mscrpc.server;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.RpcResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Description 处理客户端请求的线程
 * @Author MSC419
 * @Date 2022/3/29 21:52
 * @Version 1.1
 */
@Data
@AllArgsConstructor
@Slf4j
public class ClientMessageHandlerThread implements Runnable{
    private Socket socket;
    private Object service;

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = invokeTargetMethod(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception:", e);
        }
    }

    /**
     * @Description 调用指定方法
     * @param rpcRequest
     * @Return
     * @Author MSC419
     * @Date 2022/4/3 10:03
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> cls = Class.forName(rpcRequest.getInterfaceName());
        //isAssignableFrom是用来判断子类和父类的关系的，或者接口实现类和接口的关系的
        //A.isAssignableFrom(B)
        //确定一个类(B)是不是继承自另一个父类(A)，一个接口实现类(B)是不是实现了另外一个接口(A)
        //客户端传入的是接口类名称，而服务端注册的是一个接口实现类（服务），此处要先判断该服务是否继承自客户端传入的接口类
        if (!cls.isAssignableFrom(service.getClass())) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_CLASS);
        }

        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
        if(method == null){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }

        return method.invoke(service, rpcRequest.getParameters());
    }


}
