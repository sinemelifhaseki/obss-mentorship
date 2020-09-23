package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.AllowedUser;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.AllowedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllowedUserService {
    private AllowedUserRepository allowedUserRepository;

    @Autowired
    public AllowedUserService(AllowedUserRepository allowedUserRepository) {
        this.allowedUserRepository = allowedUserRepository;
    }

    public AllowedUserService() {
    }

    public List<AllowedUser> findAll(){
        return allowedUserRepository.findAllByOrderByNameAsc();
    }
    public AllowedUser findByUid(String uid){
        return allowedUserRepository.findByUid(uid);
    }
    public AllowedUser findByEmail(String email){
        return allowedUserRepository.findByEmail(email);
    }
    public AllowedUser findById(Long id){
        return allowedUserRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public AllowedUser findFirstByName(String name){
        return allowedUserRepository.findFirstByName(name);
    }
}
