package org.qamock.config;

import org.qamock.api.json.UserObject;
import org.qamock.domain.Role;
import org.qamock.domain.User;
import org.qamock.security.CsrfSecurityRequestMatcher;
import org.qamock.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AccountService accountService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery("select username,role from user_roles where username=?");

        prepopulateData();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin")
                    .failureUrl("/login?error")
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()

                .exceptionHandling()
                    .accessDeniedPage("/forbidden")

                .and()
                .csrf()
                    .requireCsrfProtectionMatcher(new CsrfSecurityRequestMatcher())

//                <intercept-url pattern="/admin/dynamic/.+/delete" access="hasRole('ROLE_ADMIN', 'DYN_DELETE')" />
//        <intercept-url pattern="/admin/dynamic/.+/response/.+/delete" access="hasRole('ROLE_ADMIN', 'DYN_DELETE')" />
//        <intercept-url pattern="/static/**" access="permitAll" />
//        <intercept-url pattern="/admin" access="hasRole('ROLE_USER')" />
//        <intercept-url pattern="/admin/tools/**" access="hasRole('ROLE_USER')" />
//        <intercept-url pattern="/admin/dynamic/**" access="hasRole('ROLE_USER')" />
//        <intercept-url pattern="/admin/accounts/**" access="hasRole('ROLE_ADMIN')" />

                .and()
                .authorizeRequests(req ->
                        req.mvcMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
//                                .mvcMatchers("/api/**").access("hasRole('ADMIN') and hasRole('API')")
                                .mvcMatchers("/static/**").permitAll()
                                .mvcMatchers("/h2/**").permitAll()
                                .mvcMatchers("/mock/**").permitAll())

                .headers()
                    .frameOptions().sameOrigin()

                .and()
                .logout()
                    .logoutUrl("/login?logout");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void prepopulateData() {
        User user = accountService.getAccount("admin");
        if (user == null) {
            UserObject userObject = new UserObject();
            userObject.setUsername("admin");
            userObject.setPassword("1234");
            userObject.setEmail("admin@mail");
            userObject.setEnabled(true);

            List<Role> roleList = new ArrayList<>();
            roleList.add(new Role("admin", "ROLE_ADMIN"));
            userObject.setRoles(roleList);

            accountService.addAccount(userObject);
        }
    }
}
