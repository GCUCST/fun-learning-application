package cn.cst.utils;

import java.util.Date;
import lombok.Data;

@Data
public class Payload<T> {
  private String id;
  private Date expiration;
  private T userInfo;
}
