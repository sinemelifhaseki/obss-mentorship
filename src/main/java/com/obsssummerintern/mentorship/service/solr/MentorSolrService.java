package com.obsssummerintern.mentorship.service.solr;

import com.obsssummerintern.mentorship.domain.AcceptedMentor;
import com.obsssummerintern.mentorship.domain.solr.Mentor;
import com.obsssummerintern.mentorship.repository.solr.MentorSolrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorSolrService {
    private MentorSolrRepository mentorSolrRepository;

    @Autowired
    public MentorSolrService(MentorSolrRepository mentorSolrRepository){
        this.mentorSolrRepository = mentorSolrRepository;
    }

    public void deleteById(Mentor mentor){
        mentorSolrRepository.delete(mentor);
    }

    public void deleteAll(){
        mentorSolrRepository.deleteAll();
    }

    public List<Mentor> findAllByDetails(String text){
        return mentorSolrRepository.findAllByDetails(text);
    }

    public Mentor findByMentorid(Long mentorid){
        return mentorSolrRepository.findByMentorid(mentorid);
    }
    public void save(Long id, String name, String details, String email, String topic, String subtopic){
        Mentor mentor = new Mentor(id, name, details, email, topic,subtopic);
        mentorSolrRepository.save(mentor);
    }
    public void save(AcceptedMentor acceptedMentor){
        Mentor mentor = new Mentor(acceptedMentor);
        mentorSolrRepository.save(mentor);
    }
}
