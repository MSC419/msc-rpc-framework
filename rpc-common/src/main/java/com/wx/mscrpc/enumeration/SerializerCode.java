package com.wx.mscrpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 16:44
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO((byte)0),
    JSON((byte)1),
    HESSIAN((byte)2);

    private final byte code;

}
