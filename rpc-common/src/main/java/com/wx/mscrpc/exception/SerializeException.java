package com.wx.mscrpc.exception;

/**
 * @Description 异常类：使用Kryo序列化时出现异常
 * @Author MSC419
 * @Date 2022/4/4 19:36
 * @Version 2.0
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String message) {
        super(message);
    }
}
