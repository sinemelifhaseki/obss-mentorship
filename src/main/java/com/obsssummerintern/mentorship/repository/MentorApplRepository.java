package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.MentorApplication;
import com.obsssummerintern.mentorship.domain.RegularUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MentorApplRepository extends CrudRepository<MentorApplication, Long> {
    List<MentorApplication> findAllByOrderByNameAsc();
    List<MentorApplication> findAllByUser(RegularUser regularUser);
}
