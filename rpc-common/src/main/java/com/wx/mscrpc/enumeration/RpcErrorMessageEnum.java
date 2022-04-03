package com.wx.mscrpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/4/3 9:37
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessageEnum {
    SERVICE_INVOCATION_FAILURE("服务调用失败"),//注意这里是逗号
    SERVICE_CAN_NOT_BE_NULL("注册的服务不能为空");

    private final String message;
}

