package cn.cst.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class SysUser implements UserDetails, Serializable {
  List<SysRole> roleList = new ArrayList<>();
  private String username;
  private String password;
  private String status;
  private String id;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roleList;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  //    @JsonIgnore // 很重要 针对重写的方法，不要参与
  public boolean isEnabled() {
    return true;
  }
}
