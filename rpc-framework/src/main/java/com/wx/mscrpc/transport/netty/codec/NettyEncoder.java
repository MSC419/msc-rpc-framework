package com.wx.mscrpc.transport.netty.codec;

import com.wx.mscrpc.dto.RpcMessage;
import com.wx.mscrpc.dto.RpcRequest;
import com.wx.mscrpc.enumeration.PackageType;
import com.wx.mscrpc.enumeration.SerializerCode;
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
public class NettyEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 通信协议格式：
     * +---------------------------------------------------------------+
     * | 魔数 4byte | 序列化算法 1byte | 报文类型 1byte |  数据长度 4byte   |
     * +---------------------------------------------------------------+
     * |                   数据内容 （长度不定）                          |
     * +---------------------------------------------------------------+
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        try {
            //魔数
            out.writeInt(MAGIC_NUMBER);
            //序列化算法
            out.writeByte(msg.getSerializeType());
            //报文类型
            out.writeByte(msg.getMessageType());
            // 1. 将对象转换为byte
            byte[] bytes = getBodyByte(msg);
            // 2. 写入消息对应的字节数组长度
            out.writeInt(bytes.length);
            // 3. 将字节数组写入 ByteBuf 对象中
            out.writeBytes(bytes);
        }catch (Exception e){
            log.error("Netty Encode error!", e);
        }
    }

    /**
     * @Description 将数据按照指定序列化方式写成字节 TODO 后面加上压缩方式
     * @param msg
     * @Return 字节数组
     * @Author MSC419
     * @Date 2022/5/23 10:53
     */
    private byte[] getBodyByte(RpcMessage msg) {
        byte messageType = msg.getMessageType();
        // 如果是 ping、pong 心跳类型的，没有 body，直接返回头部长度
        if (messageType == PackageType.HEARTBEAT_PACK.getCode()) {
            return null;
        }

        // 序列化器
        Serializer serializer = Serializer.getByCode(msg.getSerializeType());

        // 序列化
        byte[] bytes = serializer.serialize(msg.getData());
        return bytes;
    }

}
