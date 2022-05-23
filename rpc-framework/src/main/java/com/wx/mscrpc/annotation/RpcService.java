package com.wx.mscrpc.annotation;

import java.lang.annotation.*;

/**
 * @Description RPC 服务注解，打上这个注解的类会被扫描并且实例化后注册到注册中心
 * @Author MSC419
 * @Date 2022/5/23 17:22
 * @Version 1.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * @Description 服务名，默认值是该类的完整类名
     * @Author MSC419
     * @Date 2022/5/23 19:31
     */
    String name() default "";
}
