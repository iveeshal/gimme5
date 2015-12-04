package com.phantom.config;

/**
 * Created by vishal on 20/11/15.
 */
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/font-awesome/**").and().ignoring().antMatchers("/less/**").and().ignoring().antMatchers("favicon.ico");
    }


    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email,password, enabled from users where email=?")
                .authoritiesByUsernameQuery(
                        "select email, role from user_roles where email=?");
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().disable().
                authorizeRequests().antMatchers(HttpMethod.POST, "/idea/**").permitAll().
                antMatchers("/admin/**").authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/error")
                .usernameParameter("email").passwordParameter("password").defaultSuccessUrl("/admin-welcome")
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf().disable();
    }

}
