package com.foo.spring.boot.camunda.config;

import com.foo.spring.boot.camunda.security.jwt.JwtEntryPoint;
import com.foo.spring.boot.camunda.security.jwt.JwtTokenFilter;
import com.foo.spring.boot.camunda.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtEntryPoint unAuthorizedHandler;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter initTokenFilter() {
        return new JwtTokenFilter(tokenProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/camunda/**", "/camunda-welcome/**", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .logout().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers("/",
                        "/camunda/**", "/cammunda-welcome/**", "/h2-console/**",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/swagger-resources/**",
                        "/v2/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unAuthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(initTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
