package cn.cst.entities;

import java.io.Serializable;
import lombok.*;
import lombok.Data;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
  public String name;
  public String pass;
}
