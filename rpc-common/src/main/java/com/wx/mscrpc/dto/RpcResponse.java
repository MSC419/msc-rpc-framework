package com.wx.mscrpc.dto;

import com.wx.mscrpc.enumeration.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * @Description rpc将返回数据包装成RpcResponse类
 * 其中包含：响应码（标识调用是否成功）、响应消息（对响应码的解释）、
 * 响应数据（调用成功后应返回的数据）
 * @Author MSC419
 * @Date 2022/4/3 9:07
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1940650262445635825L;

    private Integer code;//响应码
    private String message;//响应消息
    private T data;//响应数据

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        if(data != null){
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }

}
