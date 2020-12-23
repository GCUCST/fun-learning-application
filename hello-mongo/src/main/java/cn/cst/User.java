package cn.cst;

import java.io.Serializable;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
  String name;
  Integer age;
}
