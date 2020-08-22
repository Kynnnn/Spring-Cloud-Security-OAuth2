package com.kynnnn.security.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 令牌存储配置
 * @author Zhudiwei
 * @date 2020/08/22
 **/
@Configuration
public class TokenConfig {

    @Bean
    public TokenStore tokenStore(){
        //内存方式，生成普通令牌
        return new InMemoryTokenStore();
    }
}
