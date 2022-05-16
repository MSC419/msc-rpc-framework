package com.wx.mscrpc.api;

import lombok.*;

import java.io.Serializable;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/5 20:16
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String name;
    private String message;
}
