package org.qamock.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        logger.info("configAuthentication call");

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery(
                        "select username,role from user_roles where username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/admin").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/tools/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/dynamic/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/dynamic/{\\d+}/delete").access("hasRole('ROLE_ADMIN', 'DYN_DELETE')")
                .antMatchers("/admin/dynamic/{\\d+}/response/{\\d+}/delete").access("hasRole('ROLE_ADMIN', 'DYN_DELETE')")
                //.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                //.anyRequest().permitAll()
        .and()
            .formLogin().loginPage("/login").failureUrl("/login?error")
            .usernameParameter("username").passwordParameter("password")
        .and()
            .logout().logoutSuccessUrl("/login?logout")
        .and()
            .exceptionHandling().accessDeniedPage("/forbidden")
        .and()
            .csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
                private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
                @Override
                public boolean matches(HttpServletRequest httpServletRequest) {

                    if(allowedMethods.matcher(httpServletRequest.getMethod()).matches()){
                        return false;
                    }

                    if(httpServletRequest.getRequestURI().contains("/dynamic/resource/")){
                        return false;
                    }

                    if(httpServletRequest.getRequestURI().contains("/h2-console")){
                        return false;
                    }

                    return true;
                }
            })
        .and()
            .headers()
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable()
        ;

        /*http.formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();*/
    }


    /*@Configuration
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {

            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER");

            auth
                    .inMemoryAuthentication()
                    .withUser("admin1").password("1234").roles("ADMIN");
        }

    }*/
}
