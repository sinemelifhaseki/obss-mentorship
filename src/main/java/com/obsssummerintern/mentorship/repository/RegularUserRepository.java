package com.obsssummerintern.mentorship.repository;


import com.obsssummerintern.mentorship.domain.RegularUser;
import org.springframework.data.repository.CrudRepository;

public interface RegularUserRepository extends CrudRepository<RegularUser,Long> {
    RegularUser findFirstByUid(String uid);
}
