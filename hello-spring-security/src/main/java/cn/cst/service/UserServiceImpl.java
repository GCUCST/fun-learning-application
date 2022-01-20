package cn.cst.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserDetailsService {
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<SimpleGrantedAuthority> authorities_admin = new ArrayList<>();
    authorities_admin.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    List<SimpleGrantedAuthority> authorities_guest = new ArrayList<>();
    authorities_guest.add(new SimpleGrantedAuthority("ROLE_GUEST"));
    if (username.equals("admin")) // 数据库查找,并且转为springSecurity的对象
    return new User("admin", new BCryptPasswordEncoder().encode("123"), authorities_admin);
    if (username.equals("guest")) // 数据库查找,并且转为springSecurity的对象
    return new User("guest", new BCryptPasswordEncoder().encode("123"), authorities_guest);
    else return null;
  }
}
