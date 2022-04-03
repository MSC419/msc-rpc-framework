package com.wx.mscrpc.exception;

import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;

/**
 * @Description 异常类：抛出rpc运行时异常
 * @Author MSC419
 * @Date 2022/4/3 9:34
 * @Version 1.1
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail){
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum){
        super(rpcErrorMessageEnum.getMessage());
    }
}
