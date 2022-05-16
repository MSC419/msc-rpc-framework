package com.wx.mscrpc.transport.netty.codec;

import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 16:32
 * @Version 1.0
 */
@Slf4j
public class NettyEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final Serializer serializer;

    public NettyEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 通信协议头：
     * +---------------------------------------------------------------+
     * | 魔数 4byte | 序列化算法 1byte | 报文类型 1byte |  数据长度 4byte   |
     * +---------------------------------------------------------------+
     * |                   数据内容 （长度不定）                          |
     * +---------------------------------------------------------------+
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            //魔数
            out.writeInt(MAGIC_NUMBER);
            //序列化算法
            out.writeByte(serializer.getCode());
            //报文类型
            if (msg instanceof RpcRequest) {
                out.writeByte(PackageType.REQUEST_PACK.getCode());
            } else {
                out.writeByte(PackageType.RESPONSE_PACK.getCode());
            }
            // 1. 将对象转换为byte
            byte[] bytes = serializer.serialize(msg);
            // 2. 写入消息对应的字节数组长度
            out.writeInt(bytes.length);
            // 3. 将字节数组写入 ByteBuf 对象中
            out.writeBytes(bytes);
        }catch (Exception e){
            log.error("Netty Encode error!", e);
        }
    }
}
