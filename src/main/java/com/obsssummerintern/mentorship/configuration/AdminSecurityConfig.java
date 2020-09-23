package com.obsssummerintern.mentorship.configuration;

import com.obsssummerintern.mentorship.domain.AllowedUser;
import com.obsssummerintern.mentorship.domain.GoogleUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception{

        http
                .authorizeRequests()
                    .antMatchers("/admin").hasAuthority("ADMİNS")
                    .antMatchers("/user").hasAuthority("USERS")
                    .antMatchers("/").permitAll()
                    .antMatchers("/login/***").permitAll()
                    .anyRequest().fullyAuthenticated()
                //.and()
                //    .oauth2Login()
                .and()
                    //.oauth2Login()
                    /*.formLogin()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .permitAll();*/
                .formLogin()
                    .defaultSuccessUrl("/", true)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/login")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                           // .userAuthoritiesMapper(this.userAuthoritiesMapper());
                            .customUserType(GoogleUser.class, "google");


    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=obssmentorship,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");
    }
}
