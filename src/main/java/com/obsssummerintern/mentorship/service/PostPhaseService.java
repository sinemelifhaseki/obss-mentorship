package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.Phase;
import com.obsssummerintern.mentorship.domain.PostPhase;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.PostPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostPhaseService {
    private PostPhaseRepository postPhaseRepository;
    private PhaseService phaseService;
    private UserService userService;

    @Autowired
    public PostPhaseService(PostPhaseRepository postPhaseRepository, PhaseService phaseService, UserService userService) {
        this.postPhaseRepository = postPhaseRepository;
        this.phaseService = phaseService;
        this.userService = userService;
    }

    public PostPhaseService() {
    }
    /////////////////////////////////////////////////////////////////////////
    public PostPhase findById(Long id){
        return postPhaseRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public void deleteById(Long id){
        postPhaseRepository.deleteById(id);
    }
    public List<PostPhase> findByPhase(Phase phase){ // 1 by mentor, 1 by mentee, both have same Phase
        return postPhaseRepository.findAllByPhase(phase);
    }

    public void createAndSaveRating(PostPhase postPhase){
        postPhaseRepository.save(postPhase);
    }

}
