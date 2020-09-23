package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.*;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.MentorshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorshipService {
    MentorshipRepository mentorshipRepository;
    AcceptedMentorService acceptedMentorService;
    TopicService topicService;
    SubtopicService subtopicService;
    PhaseService phaseService;
    UserService userService;

    public MentorshipService() {
    }
    @Autowired
    public MentorshipService(MentorshipRepository mentorshipRepository, AcceptedMentorService acceptedMentorService, TopicService topicService, SubtopicService subtopicService, PhaseService phaseService, UserService userService) {
        this.mentorshipRepository = mentorshipRepository;
        this.acceptedMentorService = acceptedMentorService;
        this.topicService = topicService;
        this.subtopicService = subtopicService;
        this.phaseService = phaseService;
        this.userService = userService;
    }

    public Mentee findById(Long id){
       return mentorshipRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteById(Long id){
        mentorshipRepository.deleteById(id);
    }

    public Long createAndSaveMentorship(Long topicId, Long subtopicId, Long mentorId, String name, String email){
        Topic topic = topicService.findById(topicId);
        Subtopic subtopic = subtopicService.findById(subtopicId);
        AcceptedMentor acceptedMentor = acceptedMentorService.findById(mentorId);
        RegularUser user = userService.getCurrentUser();
        Mentee mentorship = new Mentee(name, email, acceptedMentor, topic, subtopic, user, "Başlamadı");
        mentorshipRepository.save(mentorship);

        return topic.getId();
    }

    public List<Mentee> findAllByUser(RegularUser user){
        return mentorshipRepository.findAllByUser(user);
    }

    public List<Mentee> findAllByAcceptedmentor(AcceptedMentor acceptedMentor){
        return mentorshipRepository.findAllByAcceptedMentor(acceptedMentor);
    }
    public void updateMentorship(Mentee m){
        mentorshipRepository.save(m);
    }

    public List<Mentee> findAll(){
        return mentorshipRepository.findAllByOrderByNameAsc();
    }
}
