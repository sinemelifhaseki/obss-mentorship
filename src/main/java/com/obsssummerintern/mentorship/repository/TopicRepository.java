package com.obsssummerintern.mentorship.repository;

import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends CrudRepository<Topic,Long> {
    List<Topic> findAllByOrderByTitleAsc();
    List<Subtopic> getSubtopicsForTopicById(@Param("id") Long id);

}
