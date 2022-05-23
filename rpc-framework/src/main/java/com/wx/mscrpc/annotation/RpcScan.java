package com.wx.mscrpc.annotation;

import java.lang.annotation.*;

/**
 * @Description 指示哪里需要扫描
 * @Author MSC419
 * @Date 2022/5/23 17:34
 * @Version 1.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcScan {
    /**
     * 扫描范围的根包名，默认为启动类的包名
     * （如果为空，会将启动类的包名赋给它）
     */
    String value() default "";
}
