package com.nisshoku.mgnt.config;

import com.nisshoku.mgnt.exceptions.CustomAccessDeniedHandler;
import com.nisshoku.mgnt.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final SimpleUrlAuthenticationFailureHandler failureHandler;
    private final SimpleUrlAuthenticationSuccessHandler successHandler;

    public SecurityJavaConfig(final RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                              final CustomAccessDeniedHandler accessDeniedHandler) {
        super();

        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;

        failureHandler = new SimpleUrlAuthenticationFailureHandler();
        successHandler = new SimpleUrlAuthenticationSuccessHandler();

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("admin")
                    .password(encoder.encode("adminPassword"))
                    .roles("ADMIN")
                .and()
                .withUser("employee")
                .password(encoder.encode("employeePassword"))
                    .roles("EMPLOYEE");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                //.authorizeRequests()
                //.and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/api/v1/employees/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/v1/projects/**").permitAll()
                    .antMatchers( "/api/v1/tasks/**").authenticated()
                    .antMatchers("/api/v1/employees/**").hasRole("ADMIN")
                    .antMatchers("/api/v1/projects/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .httpBasic()
                .and()
                .logout();
    }
}
