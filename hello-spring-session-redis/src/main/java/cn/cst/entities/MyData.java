package cn.cst.entities;

import java.io.Serializable;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyData implements Serializable {
  public String code;
  public String msg;
}
