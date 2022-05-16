package com.wx.mscrpc.api;

import lombok.*;

import java.io.Serializable;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 11:44
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User implements Serializable {
    private Integer id;
    private String userName;
    private Boolean sex;
}
