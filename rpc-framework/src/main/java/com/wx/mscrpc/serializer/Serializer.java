package com.wx.mscrpc.serializer;

/**
 * @Description 序列化接口，所有序列化类都要实现这个接口
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
     * @param clazz 目标类
     * @Return 反序列化后的对象
     * @Author MSC419
     * @Date 2022/4/4 17:22
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    static Serializer getByCode(byte code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            default:
                return null;
        }
    }

    byte getCode();
}
