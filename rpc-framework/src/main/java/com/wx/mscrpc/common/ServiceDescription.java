package com.wx.mscrpc.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 21:22
 * @Version 1.0
 */
@Data
@Builder
public class ServiceDescription implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
