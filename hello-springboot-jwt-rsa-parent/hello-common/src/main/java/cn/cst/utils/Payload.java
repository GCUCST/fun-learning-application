package cn.cst.utils;

import lombok.Data;

import java.util.Date;

@Data
public class Payload<T> {
    private String id;
    private Date expiration;
    private T userInfo;
}
