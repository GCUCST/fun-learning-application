package cn.cst.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {

  @ExceptionHandler(RuntimeException.class)
  public String handException(Exception e) {
    System.out.println("进入异常控制");
    if (e instanceof AccessDeniedException) {
      return "权限不足";
    } else return "服务器内部错误";
  }
}
