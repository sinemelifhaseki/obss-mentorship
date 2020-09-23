package com.obsssummerintern.mentorship.controller;

import com.obsssummerintern.mentorship.domain.Mentee;
import com.obsssummerintern.mentorship.domain.MentorApplication;
import com.obsssummerintern.mentorship.domain.Phase;
import com.obsssummerintern.mentorship.domain.PostPhase;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.repository.MentorshipRepository;
import com.obsssummerintern.mentorship.service.MentorshipService;
import com.obsssummerintern.mentorship.service.PhaseService;
import com.obsssummerintern.mentorship.service.PostPhaseService;
import com.obsssummerintern.mentorship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/user")
public class MentorshipController {
    private MentorshipService mentorshipService;
    private PhaseService phaseService;
    private UserService userService;
    private PostPhaseService postPhaseService;

    @Autowired
    public MentorshipController(MentorshipService mentorshipService, PhaseService phaseService, UserService userService,PostPhaseService postPhaseService) {
        this.mentorshipService = mentorshipService;
        this.phaseService = phaseService;
        this.userService = userService;
        this.postPhaseService = postPhaseService;
    }

    public boolean adminChecker(){  // RETURNS FALSE IF THE LOGGED IN USER IS NOT ADMIN
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMİNS"));
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    //           MENTORSHIP DETAILS                //
    @RequestMapping("/mentorship-details/{id}")
    public String mentorshipDetails(@PathVariable("id") Long mentorshipId, Model model) throws NotFoundException {
        Mentee mentee = mentorshipService.findById(mentorshipId);
        List<Phase> phases = phaseService.getPhasesByMenteeId(mentorshipId);
        for(Phase p: phases){
            List<PostPhase> postPhases = postPhaseService.findByPhase(p);
            if(postPhases.size()>=2){
                for(PostPhase pp: postPhases){
                    if(pp.getByMentor()){
                        p.setMentorFeedback(pp.getFeedback());
                        p.setMentorRating(pp.getStars());
                    }
                    else{
                        p.setMenteeFeedback(pp.getFeedback());
                        p.setMenteeRating(pp.getStars());
                    }
                }
            }else if(postPhases.size()==1){
                for(PostPhase pp: postPhases){
                    if(pp.getByMentor()){
                        p.setMentorRating(pp.getStars());
                        p.setMentorFeedback(pp.getFeedback());
                    }
                    else{
                        p.setMenteeFeedback(pp.getFeedback());
                        p.setMenteeRating(pp.getStars());
                    }
                }
            }else{
                p.setMentorRating("-");
                p.setMentorFeedback("-");
                p.setMenteeRating("-");
                p.setMenteeFeedback("-");
            }
        }
        model.addAttribute("phases", phases);
        model.addAttribute("mentorship", mentee);
        if(mentee.getUser().equals(userService.getCurrentUser())){
            model.addAttribute("mail",mentee.getAcceptedMentor().getEmail());
        }else{
            model.addAttribute("mail",mentee.getUser().getEmail());
        }
        if(mentee.getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || mentee.getUser().equals(userService.getCurrentUser())){
            return "mentorship/mentorship-details.html";
        }
        if(adminChecker()){
            return "admin/unauthorized";
        }

        return "user/unauthorized";
    }


    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    //            PROCESS PLANNING                //
    @GetMapping("/mentorship-details/plan-process/{id}")
    public String planProcess(@PathVariable("id") Long mentorshipId, Model model) throws NotFoundException {
        Mentee mentee = mentorshipService.findById(mentorshipId);
        List<Phase> phases = phaseService.getPhasesByMenteeId(mentorshipId);
        model.addAttribute("phases", phases);
        model.addAttribute("mentorship", mentee);
        model.addAttribute("newphase",new Phase());
        if(adminChecker()){
            return "user/unauthorized";
        }
        if(mentee.getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || mentee.getUser().equals(userService.getCurrentUser())){
            return "mentorship/plan-process.html";
        }
        return "user/unauthorized";
    }

    //              ADD PHASE                   //
    @PostMapping("/mentorship-details/plan-process/{id}")
    public String addPhase(@PathVariable("id") Long mentorshipId, @ModelAttribute Phase newphase, Model model) throws NotFoundException {
        Mentee mentee = mentorshipService.findById(mentorshipId);
        List<Phase> phases = phaseService.getPhasesByMenteeId(mentorshipId);
        model.addAttribute("phases", phases);
        model.addAttribute("mentorship", mentee);
        newphase.setStatus("Başlamadı");
        phaseService.createAndSavePhase(mentee, newphase.getEndDate(),newphase.getTitle(),newphase.getStatus(),newphase.getPhaseNumber());
        if(adminChecker()){
            return "user/unauthorized";
        }
        return "redirect:/user/mentorship-details/plan-process/"+mentorshipId;
    }

    //             DELETE PHASE            //
    @RequestMapping("/delete-phase/{mentorshipid}/{phaseid}")
    public String deletePhase(@PathVariable("mentorshipid") Long mentorshipId, @PathVariable("phaseid") Long phaseId, Model model) throws NotFoundException{
        Mentee mentee = mentorshipService.findById(mentorshipId);
        if(mentee.getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || mentee.getUser().equals(userService.getCurrentUser())){
            phaseService.deleteById(phaseId);
            return "redirect:/user/mentorship-details/plan-process/"+mentorshipId;
        }
        return "user/unauthorized";
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    /*@RequestMapping("/find-mentor")
    public String findMentor(Model model){

    }*/
    //              START PROCESS            //
    @RequestMapping("/start-process/{id}")
    public String startProcess(@PathVariable("id") Long mentorshipId, Model model) throws NotFoundException {
        Mentee mentee = mentorshipService.findById(mentorshipId);
        List<Phase> phases = phaseService.getPhasesByMenteeId(mentorshipId);
        model.addAttribute("phases", phases);
        model.addAttribute("mentorship", mentee);
        if(mentee.getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || mentee.getUser().equals(userService.getCurrentUser())){
            mentee.setPhaseStatus("1");
            mentorshipService.updateMentorship(mentee);
            for(Phase p: phases){
                if(p.getStatus().equals("Başlamadı")&&p.getPhaseNumber().equals("1")){
                    p.setStatus("Aktif");
                    phaseService.updatePhase(p);
                }
            }
            return "redirect:/user/mentorship-details/"+mentorshipId;
        }
        if(adminChecker()){
            return "admin/unauthorized";
        }
        return "user/unauthorized";
    }

    //         END PHASE       //
    @RequestMapping("/end-phase/{mentorshipid}/{phaseid}")
    public String endPhase(@PathVariable("mentorshipid") Long mentorshipId, @PathVariable("phaseid") Long phaseId, Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        Mentee mentee = mentorshipService.findById(mentorshipId);
        if(!(mentee.getUser().equals(userService.getCurrentUser()) || mentee.getAcceptedMentor().getUser().equals(userService.getCurrentUser()))){
            return "user/unauthorized";
        }
        List<Phase> phases = phaseService.getPhasesByMenteeId(mentorshipId);
        Integer nextPhaseNo = Integer.valueOf("0"); // initialization
        boolean lastPhaseFlag = true;
        for(Phase p: phases){
            if(p.getPhaseNumber().equals(String.valueOf(phaseService.findById(phaseId).getPhaseNumber())) && p.getStatus().equals("Aktif")){
                p.setStatus("Tamamlandı");
                phaseService.updatePhase(p);
                nextPhaseNo = Integer.valueOf(p.getPhaseNumber())+1;
            }
            else if(p.getPhaseNumber().equals(String.valueOf(nextPhaseNo))){
                p.setStatus("Aktif");
                phaseService.updatePhase(p);
                mentee.setPhaseStatus(p.getPhaseNumber());
                mentorshipService.updateMentorship(mentee);
                lastPhaseFlag = false;
            }
        }
        for(Phase p: phases){
            if(p.getStatus().equals("Aktif")){
                mentee.setPhaseStatus(p.getPhaseNumber());
                lastPhaseFlag=false;
            }
        }
        if(lastPhaseFlag){//there was no next phase, it was the last one: set mentorship status as finished
            mentee.setPhaseStatus("Tamamlandı");
            mentorshipService.updateMentorship(mentee);
        }

        return "redirect:/user/rating/"+phaseId;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    @GetMapping("/rating/{phaseid}")
    public String ratingForm(@PathVariable("phaseid") Long phaseId, Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        Phase phase = phaseService.findById(phaseId);
        if(!(phase.getMentee().getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || phase.getMentee().getUser().equals(userService.getCurrentUser()))){
            return "user/unauthorized";
        }
        model.addAttribute("phase",phase);
        model.addAttribute("postphase", new PostPhase());
        return "user/rating";
    }

    @PostMapping("/rating/{phaseid}")
    public String saveRatingForm(@PathVariable("phaseid") Long phaseId, @ModelAttribute PostPhase postPhase){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        Phase phase = phaseService.findById(phaseId);
        if(!(phase.getMentee().getAcceptedMentor().getUser().equals(userService.getCurrentUser()) || phase.getMentee().getUser().equals(userService.getCurrentUser()))){
            return "user/unauthorized";
        }
        postPhase.setPhase(phase);
        if(phase.getMentee().getUser().equals(userService.getCurrentUser())){
            postPhase.setByMentor(false);
        }
        else{
            postPhase.setByMentor(true);
        }
        postPhaseService.createAndSaveRating(postPhase);
        return "redirect:/user/mentorship-details/"+phase.getMentee().getId();
    }
}
