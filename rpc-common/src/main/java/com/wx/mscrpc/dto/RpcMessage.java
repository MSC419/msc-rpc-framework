package com.wx.mscrpc.dto;

import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.enumeration.SerializerCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description RPC通用信息结构
 * @Author MSC419
 * @Date 2022/5/22 17:42
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcMessage implements Serializable {
    /**
     *消息类型 {@link PackageType#getCode()}
     */
    private byte messageType;

    /**
     * 序列化类型 {@link SerializerCode#getCode()}
     */
    private byte serializeType;

    /**
     * rpcRequest和rpcResponse可以放在里面
     */
    private Object data;

}
