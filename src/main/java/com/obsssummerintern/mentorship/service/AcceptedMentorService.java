package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.*;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.AcceptedMentorRepository;
import com.obsssummerintern.mentorship.service.solr.MentorSolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcceptedMentorService {
    AcceptedMentorRepository acceptedMentorRepository;
    TopicService topicService;
    SubtopicService subtopicService;
    UserService userService;
    MentorSolrService mentorSolrService;

    @Autowired
    public AcceptedMentorService(AcceptedMentorRepository acceptedMentorRepository, TopicService topicService, SubtopicService subtopicService, UserService userService,MentorSolrService mentorSolrService) {
        this.acceptedMentorRepository = acceptedMentorRepository;
        this.topicService = topicService;
        this.subtopicService = subtopicService;
        this.userService = userService;
        this.mentorSolrService = mentorSolrService;
    }

    public AcceptedMentorService() {}

    public AcceptedMentor findById(Long id){
       return acceptedMentorRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteById(Long id){
        mentorSolrService.deleteById(mentorSolrService.findByMentorid(id));
        acceptedMentorRepository.deleteById(id);
    }

    public Long createAndSaveAcceptedMentor(Long topicId, Long subtopicId, String name, String details, String email, RegularUser user){
        Topic topic = topicService.findById(topicId);
        Subtopic subtopic = subtopicService.findById(subtopicId);
        AcceptedMentor acceptedMentor = new AcceptedMentor(name, details, email, topic, subtopic, user);
        acceptedMentorRepository.save(acceptedMentor);

        return topic.getId();
    }

    public List<AcceptedMentor> getAll(){
        List<AcceptedMentor> acceptedMentors = acceptedMentorRepository.findAllByOrderByNameAsc();
        return acceptedMentors;}

    public List<AcceptedMentor> findAllByUser(RegularUser user){
        return acceptedMentorRepository.findAllByUser(user);
    }

    public AcceptedMentor findTopByOrderByIdDesc(){
        return acceptedMentorRepository.findTopByOrderByIdDesc();
    }
    public List<AcceptedMentor> findAllByTopic(Topic topic){
        return acceptedMentorRepository.findAllByTopic(topic);
    }
    public List<AcceptedMentor> findAllBySubtopic(Subtopic subtopic){
        return acceptedMentorRepository.findAllBySubtopic(subtopic);
    }
}
