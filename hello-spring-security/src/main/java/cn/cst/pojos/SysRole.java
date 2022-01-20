package cn.cst.pojos;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class SysRole implements GrantedAuthority {

  private String id;
  private String roleName;
  private String desc;

  @Override
  public String getAuthority() {
    return roleName;
  }
}
