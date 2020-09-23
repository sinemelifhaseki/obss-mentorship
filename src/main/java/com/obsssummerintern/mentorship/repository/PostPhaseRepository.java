package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.Phase;
import com.obsssummerintern.mentorship.domain.PostPhase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostPhaseRepository extends CrudRepository<PostPhase,Long> {
    List<PostPhase> findAllByPhase(Phase phase); // 1 by mentor, 1 by mentee, both have same Phase
}
