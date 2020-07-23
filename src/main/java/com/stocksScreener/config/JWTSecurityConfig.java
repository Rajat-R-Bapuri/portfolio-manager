package com.stocksScreener.config;

import com.stocksScreener.filter.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTFilter jwtFilter;

    @Autowired
    public JWTSecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().httpBasic().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(this.jwtFilter, BasicAuthenticationFilter.class);
    }
}
