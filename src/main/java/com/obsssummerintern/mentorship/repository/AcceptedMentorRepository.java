package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.AcceptedMentor;
import com.obsssummerintern.mentorship.domain.RegularUser;
import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AcceptedMentorRepository extends CrudRepository<AcceptedMentor, Long> {
    List<AcceptedMentor> findAllByOrderByNameAsc();
    List<AcceptedMentor> findAllByUser(RegularUser user);
    AcceptedMentor findTopByOrderByIdDesc();
    List<AcceptedMentor> findAllByTopic(Topic topic);
    List<AcceptedMentor> findAllBySubtopic(Subtopic subtopic);
}
