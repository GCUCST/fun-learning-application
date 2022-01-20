package cn.cst.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userService;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public SecurityConfig(UserDetailsService userService) {
    this.userService = userService;
  }

  // 认证用户的来源
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.inMemoryAuthentication()
    //                .withUser("cst")
    //                .password("{noop}123")//{noop}代表原文
    //                .roles("ADMIN");
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
  }

  // 相关信息，拦截规则
  //    @Override
  //    protected void configure(HttpSecurity http) throws Exception {
  //        http.rememberMe();
  //        http.csrf().disable();
  ////        http.formLogin().loginPage("/my-login-page").loginProcessingUrl(); //定制登录页面
  //        http.formLogin();
  //        http.logout().logoutSuccessUrl("http://www.baidu.com");
  //        http
  //                .authorizeRequests()
  //                .antMatchers("/get/guest").permitAll() // 匹配到就放行
  //                .antMatchers("/get/admin").hasAnyRole("admin");
  //    }

}
