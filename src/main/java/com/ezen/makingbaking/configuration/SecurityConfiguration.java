package com.ezen.makingbaking.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
// security의 filterchain을 구현하기 위한 어노테이션
@EnableWebSecurity
public class SecurityConfiguration {

}
