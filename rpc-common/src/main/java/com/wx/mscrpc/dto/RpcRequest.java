package com.wx.mscrpc.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @Description rpc将请求数据包装成RpcRequest类
 * 其中包含属性：接口名、方法名、返回值类型、传入参数
 * 这四个属性可以唯一确定一个方法，这个方法就是rpc希望调用的方法
 * @Author MSC419
 * @Date 2022/3/29 21:22
 * @Version 1.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 4457347321815052832L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
