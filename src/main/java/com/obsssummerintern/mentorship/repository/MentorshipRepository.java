package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.AcceptedMentor;
import com.obsssummerintern.mentorship.domain.Mentee;
import com.obsssummerintern.mentorship.domain.RegularUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MentorshipRepository extends CrudRepository<Mentee,Long> {
    List<Mentee> findAllByOrderByNameAsc();
    List<Mentee> findAllByUser(RegularUser user);
    List<Mentee> findAllByAcceptedMentor(AcceptedMentor acceptedMentor);
}
