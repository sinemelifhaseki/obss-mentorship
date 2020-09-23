package com.obsssummerintern.mentorship.controller;

import com.obsssummerintern.mentorship.domain.AllowedUser;
import com.obsssummerintern.mentorship.domain.RegularUser;
import com.obsssummerintern.mentorship.service.AllowedUserService;
import com.obsssummerintern.mentorship.service.LinkedInService;
import com.obsssummerintern.mentorship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
public class SecureController {

    private static final String EMAIL_ENDPOINT = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";
    private static final String CLIENT_ID ="77668qj90qju5b";
    private static final String CLIENT_SECRET ="xJm98zvHL3HAhROC";
    private static final String REDIRECT_URI = "http://localhost:8080";

    private UserService userService;
    private LinkedInService linkedInService;
    private AllowedUserService allowedUserService;

    @Autowired
    public SecureController(UserService userService, AllowedUserService allowedUserService, LinkedInService linkedInService) {
        this.userService = userService;
        this.allowedUserService = allowedUserService;
        this.linkedInService = linkedInService;
    }


    @GetMapping("/")
    public String secure(@RequestParam(required = false) String code) throws Exception {


        boolean authorized = false;
        boolean authenticated = false;

        // LinkedIn
        if(code != null){
            String userId = linkedInService.exchangeToken(code);
            if(userId != null){
                RegularUser user = userService.findByUid(userId);
                Principal principal = new Principal() {
                    @Override
                    public String getName() {
                        return user.getName();
                    }
                };
                user.setName(user.getId());
                authenticated = true;
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USERS");
                Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, Collections.singletonList(grantedAuthority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        else if(SecurityContextHolder.getContext().getAuthentication() instanceof  OAuth2AuthenticationToken){
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email").toString();

            if(allowedUserService.findByEmail(email) != null){
                //System.out.println(allowedUserService.findByEmail(email).getUid());
                authenticated = true;
            }
        }else if(SecurityContextHolder.getContext().getAuthentication() instanceof  UsernamePasswordAuthenticationToken){
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMİNS"));
            if(SecurityContextHolder.getContext().getAuthentication().getName().equals("admin") || allowedUserService.findByUid(SecurityContextHolder.getContext().getAuthentication().getName()) != null ){
                authenticated = true;
            }
        }

        if(!authenticated){
            return "user/not-authenticated";
        }


        List<AllowedUser> allowedUsers = allowedUserService.findAll();
        //if(userService.findAll().size() == 0){
        for(AllowedUser a : allowedUsers){
            RegularUser r = new RegularUser();
            r.setName(a.getName());
            r.setEmail(a.getEmail());
            r.setId(a.getUid());
            r.setUser_id(a.getId());
            userService.save(r);
        }

        if(authorized){
            return "redirect:/admin/";
        }
        return "redirect:/user/";
    }
}
