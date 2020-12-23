package cn.cst.pojos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
  private UUID id;
  private String msg;
  private Integer status;
  private Object object;
}
