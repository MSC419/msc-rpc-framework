package com.wx.mscrpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 16:39
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK((byte)0),
    RESPONSE_PACK((byte)1);

    private final byte code;

}
