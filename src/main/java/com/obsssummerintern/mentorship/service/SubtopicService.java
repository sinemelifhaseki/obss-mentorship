package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.SubtopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubtopicService {
    SubtopicRepository subtopicRepository;
    TopicService topicService;

    @Autowired
    public SubtopicService(SubtopicRepository subtopicRepository, TopicService topicService) {
        this.subtopicRepository = subtopicRepository;
        this.topicService = topicService;
    }

    public void deleteById(Long id){
        subtopicRepository.deleteById(id);
    }

    public Subtopic findById(Long id){
        return subtopicRepository.findById(id).orElseThrow(NotFoundException::new);
    }

   /* public List<Subtopic> findAllById(List<Long> ids){
        return subtopicRepository.findAllById(ids);
    }*/

    public SubtopicService() {}

    public List<Subtopic> getSubtopicsByTopicById(Long topicid){
        return subtopicRepository.getSubtopicsByTopicId(topicid);
    }



    public Long createAndSaveSubtopic(Long topicId, String title){
        Topic topic = topicService.findById(topicId);
        Subtopic subtopic = new Subtopic(title, topic);
        subtopicRepository.save(subtopic);
        return subtopic.getId();
    }

    public List<Subtopic> getAll(){
        return subtopicRepository.findAllByOrderByTitleAsc();
    }

    public void updateSubtopic(Subtopic s){
        subtopicRepository.save(s);
    }
}
