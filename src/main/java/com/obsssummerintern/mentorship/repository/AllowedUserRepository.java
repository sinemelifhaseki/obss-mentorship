package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.AllowedUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AllowedUserRepository extends CrudRepository<AllowedUser, Long> {
    List<AllowedUser> findAllByOrderByNameAsc();
    AllowedUser findByUid(String uid);
    AllowedUser findByEmail(String email);
    AllowedUser findFirstByName(String name);
}
