package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.MentorApplication;
import com.obsssummerintern.mentorship.domain.RegularUser;
import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.MentorApplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorApplService {
    MentorApplRepository mentorApplRepository;
    TopicService topicService;
    SubtopicService subtopicService;
    UserService userService;

    @Autowired
    public MentorApplService(MentorApplRepository mentorApplRepository, TopicService topicService, SubtopicService subtopicService, UserService userService) {
        this.mentorApplRepository = mentorApplRepository;
        this.topicService = topicService;
        this.userService = userService;
        this.subtopicService = subtopicService;
    }

    public MentorApplService() {
    }

    public MentorApplication findById(Long id){
        return mentorApplRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public  void deleteById(Long id){
        mentorApplRepository.deleteById(id);
    }

    public Long createAndSaveMentorAppl(Long topicId, List<Long> subtopicIds, String name, String details, String email){
        Topic topic = topicService.findById(topicId);
        for(Long id : subtopicIds){
            Subtopic subtopic = subtopicService.findById(id);
            RegularUser user = userService.getCurrentUser();
            MentorApplication mentorApplication = new MentorApplication(name, details, email, topic, subtopic, user);
            mentorApplRepository.save(mentorApplication);
        }
        return topic.getId();
    }

    public  List<MentorApplication> findAllByUser(RegularUser regularUser){
        return mentorApplRepository.findAllByUser(regularUser);
    }
    public List<MentorApplication> getAll(){
        List<MentorApplication> mentorApplications = mentorApplRepository.findAllByOrderByNameAsc();
        return mentorApplications;
    }
    public void updateApplication(MentorApplication mentorApplication){
        mentorApplRepository.save(mentorApplication);
    }
}
