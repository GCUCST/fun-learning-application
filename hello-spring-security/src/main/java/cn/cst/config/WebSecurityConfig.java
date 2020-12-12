package cn.cst.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pass = passwordEncoder.encode("12345");
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("admin").password(pass).roles("admin")
                .and().withUser("guest").password(pass).roles("guest");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.rememberMe();
        http.csrf().disable();
//        http.formLogin().loginPage("/my-login-page").loginProcessingUrl(); //定制登录页面
        http.formLogin();
        http.logout().logoutSuccessUrl("http://www.baidu.com");
        http
                .authorizeRequests()
                .antMatchers("/get/guest").permitAll() // 匹配到就放行
                .antMatchers("/get/admin").hasAnyRole("admin");

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
