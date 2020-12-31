package cn.cst.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@EnableAuthorizationServer
@Configuration
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired private TokenStore tokenStore;
  @Autowired private ClientDetailsService clientDetailsService;
  @Autowired public AuthenticationManager authenticationManager;
  @Autowired private JwtAccessTokenConverter accessTokenConverter;
  @Autowired private AuthorizationCodeServices authorizationCodeServices;

  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new InMemoryAuthorizationCodeServices();
  }

  // 指定客户端信息的数据库来源
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("c1")
        .secret(new BCryptPasswordEncoder().encode("secret"))
        .resourceIds("res1")
        .authorizedGrantTypes(
            "authorization_code", "password", "client_credentials", "implicit", "refresh_token")
        .scopes("all")
        .autoApprove(false)
        .redirectUris("http://www.baidu.com");
  }

  @Bean
  public AuthorizationServerTokenServices tokenServices() {
    DefaultTokenServices services = new DefaultTokenServices();
    services.setClientDetailsService(clientDetailsService);
    services.setSupportRefreshToken(true);
    services.setTokenStore(tokenStore);
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
    services.setTokenEnhancer(tokenEnhancerChain);
    services.setAccessTokenValiditySeconds(7200);
    services.setRefreshTokenValiditySeconds(259200);
    return services;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(authorizationCodeServices)
        .tokenServices(tokenServices())
        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
  }

  // 检查token的策略
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("permitAll()")
        .allowFormAuthenticationForClients();
  }
}
