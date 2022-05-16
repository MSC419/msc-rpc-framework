package com.wx.mscrpc.transport.netty.codec;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.dto.RpcResponse;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.enumeration.RpcErrorMessageEnum;
import com.wx.mscrpc.exception.RpcException;
import com.wx.mscrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 16:33
 * @Version 1.0
 */
@Slf4j
public class NettyDecoder extends ByteToMessageDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try{
            //魔数是否匹配
            int magic = in.readInt();
            if(magic != MAGIC_NUMBER) {
                log.error("不识别的协议包:{}", magic);
                throw new RpcException(RpcErrorMessageEnum.UNKNOWN_PROTOCOL);
            }
            //序列化算法
            byte serializerCode = in.readByte();
            Serializer serializer = Serializer.getByCode(serializerCode);
            if(serializer == null) {
                log.error("不识别的反序列化器:{}", serializerCode);
                throw new RpcException(RpcErrorMessageEnum.UNKNOWN_SERIALIZER);
            }
            //报文类型
            byte packageCode = in.readByte();
            Class<?> packageClass;
            if(packageCode == PackageType.REQUEST_PACK.getCode()) {
                packageClass = RpcRequest.class;
            } else if(packageCode == PackageType.RESPONSE_PACK.getCode()) {
                packageClass = RpcResponse.class;
            } else {
                log.error("不识别的数据包:{}", packageCode);
                throw new RpcException(RpcErrorMessageEnum.UNKNOWN_PACKAGE_TYPE);
            }

            // 数据长度
            int length = in.readInt();
            byte[] bytes = new byte[length];
            // 填充数据
            in.readBytes(bytes);
            // 反序列化
            Object obj = serializer.deserialize(bytes, packageClass);
            out.add(obj);
        }catch (Exception e){
            log.error("Netty Decoder error!",e);
        }

    }
}
