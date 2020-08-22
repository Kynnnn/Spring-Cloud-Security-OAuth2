package com.kynnnn.security.authorization.config;

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
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Zhudiwei
 * @date 2020/08/22
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 令牌服务配置
     *
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {

        DefaultTokenServices services = new DefaultTokenServices();
        //客户端信息服务
        services.setClientDetailsService(clientDetailsService);
        //是否产生刷新令牌
        services.setSupportRefreshToken(true);
        //令牌存储策略
        services.setTokenStore(tokenStore);
        //令牌默认有效期2小时
        services.setAccessTokenValiditySeconds(7200);
        //刷新令牌默认有效期3天
        services.setRefreshTokenValiditySeconds(259200);

        return services;
    }


    /**
     * 配置客户端详情信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("resource-1")
                .secret(new BCryptPasswordEncoder().encode("password"))
                .resourceIds("res1")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials",
                        "implicit", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                //回调地址
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌访问端点
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //密码模式需要
                .authenticationManager(authenticationManager)
                //授权码模式需要
                .authorizationCodeServices(authorizationCodeServices)
                //令牌管理服务
                .tokenServices(tokenServices())
                //允许POST提交
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌端点安全策略
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //  /oauth/token_key 公开
                .tokenKeyAccess("permitAll()")
                //  /oauth/check_token 公开
                .checkTokenAccess("permitAll()")
                //允许表单验证
                .allowFormAuthenticationForClients();
    }

}
