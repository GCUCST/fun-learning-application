package cn.cst.myspringboot.util;

import io.swagger.annotations.ApiModel;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "CommonResponse", description = "通用响应体")
public class CommonResponse {
  private Integer code;
  private String msg;
  private Object payload;
  private LocalTime time;
}
