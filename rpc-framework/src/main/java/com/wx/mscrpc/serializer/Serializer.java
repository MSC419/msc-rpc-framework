package com.wx.mscrpc.serializer;

/**
 * @Description 序列化与反序列化的接口类
 * @Author MSC419
 * @Date 2022/4/4 17:17
 * @Version 2.0
 */
public interface Serializer {
    /**
     * @Description 序列化
     * @param obj 被序列化的对象
     * @Return 序列化后的字节数据
     * @Author MSC419
     * @Date 2022/4/4 17:21
     */
    byte[] serialize(Object obj);

    /**
     * @Description 反序列化
     * @param bytes 字节数据
     * @param clazz 类
     * @Return 反序列化后的对象
     * @Author MSC419
     * @Date 2022/4/4 17:22
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
