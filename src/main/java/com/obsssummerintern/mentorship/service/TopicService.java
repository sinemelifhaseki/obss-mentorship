package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public TopicService() {
    }

    public Topic findById(Long id){
        return topicRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Topic> getAll(){
        return topicRepository.findAllByOrderByTitleAsc();
    }

    public void deleteById(Long id){
        topicRepository.deleteById(id);
    }

    public List<Subtopic> getSubtopicsForTopicById(Long id){
        return topicRepository.getSubtopicsForTopicById(id);
    }

    public Long createAndSaveTopic(String title){
        Topic topic = new Topic(title);
        topicRepository.save(topic);
        return topic.getId();
    }


    public void updateTopic(Topic t){
        topicRepository.save(t);
    }
}
