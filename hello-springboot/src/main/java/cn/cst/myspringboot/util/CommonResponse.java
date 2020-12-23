package cn.cst.myspringboot.util;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
@ApiModel(value = "CommonResponse", description = "通用响应体")
public class CommonResponse {
    private Integer code;
    private String msg;
    private Object payload;
    private LocalTime time;
}
