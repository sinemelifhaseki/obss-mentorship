package com.obsssummerintern.mentorship.repository.solr;

import com.obsssummerintern.mentorship.domain.solr.Mentor;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface MentorSolrRepository extends SolrCrudRepository<Mentor, Long> {
    Mentor findByMentorid(Long mentorid);

    @Query("details:*?0*")
    List<Mentor> findAllByDetails(String text);

}
