package com.wx.mscrpc.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Description 用于解决粘包/拆包问题
 * @Author MSC419
 * @Date 2022/5/16 19:32
 * @Version 1.0
 */
public class Spliter extends LengthFieldBasedFrameDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 通信协议格式：
     * +---------------------------------------------------------------+
     * | 魔数 4byte | 序列化算法 1byte | 报文类型 1byte |  数据长度 4byte   |
     * +---------------------------------------------------------------+
     * |                   数据内容 （长度不定）                          |
     * +---------------------------------------------------------------+
     */
    private static final int LENGTH_FIELD_OFFSET = 6;
    // 数据长度
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
            ctx.channel().closeFuture();
            return null;
        }
        return super.decode(ctx, in);
    }
}
