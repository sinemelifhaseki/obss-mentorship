package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.RegularUser;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.RegularUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.PersonContextMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import java.util.List;

@Service
public class UserService implements BaseLdapNameAware {

    private RegularUserRepository regularUserRepository;
    private final LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;
    private AllowedUserService allowedUserService;

    @Autowired
    public UserService(LdapTemplate ldapTemplate, RegularUserRepository regularUserRepository, AllowedUserService allowedUserService){
        this.ldapTemplate = ldapTemplate;
        this.regularUserRepository = regularUserRepository;
        this.allowedUserService = allowedUserService;
    }

    /////////////////////////// LDAP OPERATIONS /////////////////////////////

    @Override
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public void create (RegularUser regularUser){
        Name dn = buildDn(regularUser);
        ldapTemplate.bind(dn, null, buildAttributes(regularUser));
    }

    private Name buildDn(RegularUser regularUser) {
        return LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", regularUser.getId())
                .add("cn", regularUser.getName())
                .build();
    }

    private Attributes buildAttributes(RegularUser regularUser) {
        Attributes attributes = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add("person");
        attributes.put(ocAttr);
        attributes.put("ou", "people");
        attributes.put("uid", regularUser.getId());
        attributes.put("cn", regularUser.getName());
        attributes.put("sn", regularUser.getlastName());
        attributes.put("password", regularUser.getPassword());
        return attributes;
    }

    private static class PersonContextMapper extends AbstractContextMapper<RegularUser> {
        public RegularUser doMapFromContext(DirContextOperations context) {
            RegularUser regularUser = new RegularUser();
            regularUser.setName(context.getStringAttribute("cn"));
            regularUser.setlastName(context.getStringAttribute("sn"));
            regularUser.setId(context.getStringAttribute("uid"));
            return regularUser;
        }
    }

    public List<RegularUser> findAll(){
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public RegularUser findOne(String uid){
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou","people")
                .add("uid", uid)
                .build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public boolean authenticate(String uid, String password){
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
                .and(new EqualsFilter("uid",uid))
                .and(new EqualsFilter("password", password));
        List<RegularUser> res = ldapTemplate.search(LdapUtils.emptyLdapName(),filter.encode(),new PersonContextMapper());

        return res.size() > 0;
    }



    ////////////////////////////////////////////////////////////////////////////////////
    public void save(RegularUser regularUser){
        regularUserRepository.save(regularUser);
    }
    public RegularUser findById(Long id){
        return regularUserRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public RegularUser findByUid(String uid){
        return regularUserRepository.findFirstByUid(uid);
    }
    public RegularUser getCurrentUser(){
        //if(allowedUserService.findByEmail())
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        if(findByUid(name) == null){
            if(allowedUserService.findFirstByName(name) == null){
                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email").toString();
                return findByUid(allowedUserService.findByEmail(email).getUid());
            }
            else{
                return findByUid(allowedUserService.findFirstByName(name).getUid());
            }
        }
        return findByUid(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
