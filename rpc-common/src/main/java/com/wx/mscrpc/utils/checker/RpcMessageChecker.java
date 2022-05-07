package com.wx.mscrpc.utils.checker;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.enumeration.RpcResponseCode;
import com.wx.mscrpc.exception.RpcException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 检查发送的请求和返回的响应是否匹配
 * @Author MSC419
 * @Date 2022/5/5 21:27
 * @Version 1.0
 */
@NoArgsConstructor
@Slf4j
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";

    /**
     * @param rpcRequest  发送的请求
     * @param rpcResponse 返回的响应
     * @Description 检查发送的请求和返回的响应是否匹配。因为是工具类所以要用static
     * @Author MSC419
     * @Date 2022/5/6 9:31
     */
    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            log.error("服务器调用失败，rpcResponse为null，serverName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcResponse.getRequestId().equals(rpcRequest.getRequestId())) {
            log.error("调用服务失败,rpcRequest 和 rpcResponse 对应不上,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
            log.error("服务器调用失败，serverName:{},rpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
