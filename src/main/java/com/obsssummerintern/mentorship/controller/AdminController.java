package com.obsssummerintern.mentorship.controller;

import com.obsssummerintern.mentorship.domain.AcceptedMentor;
import com.obsssummerintern.mentorship.domain.MentorApplication;
import com.obsssummerintern.mentorship.domain.Subtopic;
import com.obsssummerintern.mentorship.domain.Topic;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.service.AcceptedMentorService;
import com.obsssummerintern.mentorship.service.MentorApplService;
import com.obsssummerintern.mentorship.service.SubtopicService;
import com.obsssummerintern.mentorship.service.TopicService;
import com.obsssummerintern.mentorship.service.solr.MentorSolrService;
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
@RequestMapping("/admin")
public class AdminController {
    TopicService topicService;
    SubtopicService subtopicService;
    MentorApplService mentorApplService;
    AcceptedMentorService acceptedMentorService;
    MentorSolrService mentorSolrService;


    public boolean adminChecker(){  // RETURNS FALSE IF THE LOGGED IN USER IS NOT ADMIN
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMİNS"));
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    @Autowired
    public AdminController(TopicService topicService, SubtopicService subtopicService, MentorApplService mentorApplService, AcceptedMentorService acceptedMentorService, MentorSolrService mentorSolrService) {
        this.topicService = topicService;
        this.subtopicService = subtopicService;
        this.mentorApplService = mentorApplService;
        this.acceptedMentorService = acceptedMentorService;
        this.mentorSolrService = mentorSolrService;
    }

    //          HOME PAGE FOR ADMIN           //
    @RequestMapping("/")
    public String adminHome(Model model){
        model.addAttribute("appls",mentorApplService.getAll());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/admin-home";
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    //       MENTOR APPLICATIONS      //
    @RequestMapping("/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("appls",mentorApplService.getAll());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/admin-dashboard";
    }

    @RequestMapping("/candidate-details/{id}")
    public String candidateDetails(@PathVariable("id") Long applId, Model model) throws NotFoundException{
        MentorApplication appl = mentorApplService.findById(applId);
        model.addAttribute("appl", appl);
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/mentor-candidate-details.html";
    }

    @RequestMapping("/delete-application/{id}")
    public String deleteAppl(@PathVariable("id") Long applId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        mentorApplService.deleteById(applId);
        return "redirect:/admin/dashboard";
    }

    @RequestMapping("/accept-application/{id}")
    public String acceptAppl(@PathVariable("id") Long applId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        MentorApplication mentorApplication = mentorApplService.findById(applId);
        acceptedMentorService.createAndSaveAcceptedMentor(mentorApplication.getTopic().getId(), mentorApplication.getSubtopic().getId(), mentorApplication.getUser().getName(), mentorApplication.getDetails(), mentorApplication.getEmail(), mentorApplication.getUser());
        mentorApplService.deleteById(applId);
        mentorSolrService.save(acceptedMentorService.findTopByOrderByIdDesc());
        return "redirect:/admin/dashboard";
    }

    //     MANAGE MENTORS    //
    @RequestMapping("/manage-mentors")
    public String manageMentors(Model model) throws NotFoundException{
        List<AcceptedMentor> acceptedMentors = acceptedMentorService.getAll();
        model.addAttribute("acceptedmentors", acceptedMentors);
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/manage-mentors.html";
    }
    //      DELETE MENTOR    //
    @RequestMapping("/delete-mentor/{id}")
    public String deleteMentor(@PathVariable("id") Long mentorId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        acceptedMentorService.deleteById(mentorId);
        return "redirect:/admin/manage-mentors";
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    //           TOPIC LIST             //
    @RequestMapping("/topiclist")
    public String listTopics(Model model) {
        model.addAttribute("topics", topicService.getAll());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/topiclist";
    }


    //              TOPIC DETAILS           //
    @GetMapping("/topic-details/{id}")
    public String topicDetails(@PathVariable("id") Long topicId, Model model) throws NotFoundException{
        Topic topic = topicService.findById(topicId);
        model.addAttribute("topic", topic);
        List<Subtopic> subtopics = subtopicService.getSubtopicsByTopicById(topicId);
        model.addAttribute("subtopics", subtopics);
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/topicdetails";
    }

    //              ADD TOPIC           //
    @GetMapping("/addtopic")
    public String topicForm(Model model){
        if(!adminChecker()){
            return "user/unauthorized";
        }
        model.addAttribute("topic", new Topic());
        return "admin/addtopic";
    }

    @PostMapping("/addtopic")
    public String submitTopic(@ModelAttribute Topic topic){
        topicService.createAndSaveTopic(topic.getTitle());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "redirect:/admin/topiclist";
    }

    //           UPDATE TOPIC          //
    @GetMapping("/edit-topic/{id}")
    public String updateTopic(@PathVariable("id") Long topicId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        Topic topic = topicService.findById(topicId);
        model.addAttribute("topic", topic);
        return "admin/updatetopic";
    }

    @PostMapping("/edit-topic/{id}")
    public String saveTopic(@PathVariable("id") Long topicId, @ModelAttribute Topic topic) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        Topic previousTopic = topicService.findById(topicId);
        previousTopic.setTitle(topic.getTitle());
        topicService.updateTopic(previousTopic);
        return "redirect:/admin/topiclist";
    }

    //           DELETE TOPIC            //
    @RequestMapping("/delete-topic/{id}")
    public String deleteTopic(@PathVariable("id") Long topicId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        topicService.deleteById(topicId);
        return "redirect:/admin/topiclist";
    }


    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    //              ADD SUBTOPIC           //
    @GetMapping("/addsubtopic/{id}")
    public String subtopicForm(@PathVariable("id") Long topicId, Model model){
        Topic topic = topicService.findById(topicId);
        model.addAttribute("topic", topic);
        model.addAttribute("subtopic", new Subtopic());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "admin/addsubtopic";
    }

    @PostMapping("/addsubtopic/{id}")
    public String submitSubtopic(@PathVariable("id") Long topicId, Model model, @ModelAttribute Subtopic subtopic){
        Topic topic = topicService.findById(topicId);
        model.addAttribute("topic", topic);
        subtopicService.createAndSaveSubtopic(topicId, subtopic.getTitle());
        if(!adminChecker()){
            return "user/unauthorized";
        }
        return "redirect:/admin/topic-details/"+topicId;
    }

    //          DELETE SUBTOPIC           //
    @RequestMapping("/delete-subtopic/{topicid}/{subtopicid}")
    public String deleteSubtopic(@PathVariable("subtopicid") Long subtopicId,@PathVariable("topicid") Long topicId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        subtopicService.deleteById(subtopicId);
        return "redirect:/admin/topic-details/"+topicId;
    }

    //          EDIT SUBTOPIC            //
    @GetMapping("/edit-subtopic/{topicid}/{subtopicid}")
    public String updateSubopic(@PathVariable("subtopicid") Long subtopicId,@PathVariable("topicid") Long topicId, Model model) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        Topic topic = topicService.findById(topicId);
        Subtopic subtopic = subtopicService.findById(subtopicId);
        model.addAttribute("topic", topic);
        model.addAttribute("subtopic",subtopic);
        return "admin/updatesubtopic";
    }

    @PostMapping("/edit-subtopic/{topicid}/{subtopicid}")
    public String saveSubopic(@PathVariable("subtopicid") Long subtopicId,@PathVariable("topicid") Long topicId, @ModelAttribute Subtopic subtopic) throws NotFoundException {
        if(!adminChecker()){
            return "user/unauthorized";
        }
        Subtopic previousSubtopic = subtopicService.findById(subtopicId);
        previousSubtopic.setTitle(subtopic.getTitle());
        subtopicService.updateSubtopic(previousSubtopic);
        return "redirect:/admin/topic-details/"+topicId;
    }



}
