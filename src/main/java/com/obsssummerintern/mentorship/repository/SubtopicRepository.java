package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.Subtopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubtopicRepository extends CrudRepository<Subtopic, Long> {
    //List<Subtopic> findAllById(List<Long> ids);
    List<Subtopic> findAllByOrderByTitleAsc();
    List<Subtopic> getSubtopicsByTopicId(@Param("id") Long id);
}
