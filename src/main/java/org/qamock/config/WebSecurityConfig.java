package org.qamock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .antMatchers(HttpMethod.GET, "/admin/**").access("hasRole('ADMIN')");

        http.csrf().disable();

        /*http.authorizeRequests()
                .anyRequest().authenticated();*/

        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.exceptionHandling().accessDeniedPage("/error403");
    }


    @Configuration
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {

            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER");

            auth
                    .inMemoryAuthentication()
                    .withUser("admin").password("1234").roles("ADMIN");
        }

    }
}
