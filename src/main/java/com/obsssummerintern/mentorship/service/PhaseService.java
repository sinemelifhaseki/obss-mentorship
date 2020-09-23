package com.obsssummerintern.mentorship.service;

import com.obsssummerintern.mentorship.domain.*;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhaseService {
    PhaseRepository phaseRepository;
    //MentorshipService mentorshipService;

    public PhaseService() {
    }

    @Autowired
    public PhaseService(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
        //this.mentorshipService = mentorshipService;
    }

    public Phase findById(Long id){
        return phaseRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Phase> getPhasesByMenteeId(@Param("id") Long id){
        return phaseRepository.getPhasesByMenteeId(id);
    }

    public void deleteById(Long id){
        phaseRepository.deleteById(id);
    }

    public Long createAndSavePhase(Mentee mentorship, String endDate, String title, String status, String phaseNumber){
        //Mentee mentee = mentorshipService.findById(mentorshipId);
        Phase phase = new Phase(endDate, title, status, mentorship, phaseNumber);
        phaseRepository.save(phase);

        return mentorship.getId();
    }
    public void updatePhase(Phase p){
        phaseRepository.save(p);
    }
}
