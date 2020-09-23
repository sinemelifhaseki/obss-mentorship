package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.MentorApplication;
import com.obsssummerintern.mentorship.domain.Phase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhaseRepository extends CrudRepository<Phase,Long> {
    List<Phase> getPhasesByMenteeId(@Param("id") Long id);
}
