package com.wx.mscrpc.transport;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.RpcResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/4/3 19:31
 * @Version 1.2
 */
@Slf4j
public class RpcRequestHandler {
    public Object handle(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
            log.info("service:{} successful invoke method:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception", e);
        }
        return result;
    }

    /**
     * @Description 调用客户端请求中的方法
     * @param rpcRequest 客户端请求
     * @param service 方法
     * @Return 返回调用方法的返回值
     * @Author MSC419
     * @Date 2022/4/3 20:28
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        if (null == method) {
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
