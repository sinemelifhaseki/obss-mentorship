package com.obsssummerintern.mentorship.controller;

import com.obsssummerintern.mentorship.domain.*;
import com.obsssummerintern.mentorship.domain.solr.Mentor;
import com.obsssummerintern.mentorship.exception.NotFoundException;
import com.obsssummerintern.mentorship.service.*;
import com.obsssummerintern.mentorship.service.solr.MentorSolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserPagesController {
    private TopicService topicService;
    private SubtopicService subtopicService;
    private MentorApplService mentorApplService;
    private UserService userService;
    private MentorshipService mentorshipService;
    private AcceptedMentorService acceptedMentorService;
    private MentorSolrService mentorSolrService;
    private MailService mailService;
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    @Autowired
    public UserPagesController(TopicService topicService, SubtopicService subtopicService, MentorApplService mentorApplService, UserService userService, MentorshipService mentorshipService, AcceptedMentorService acceptedMentorService,MentorSolrService mentorSolrService, MailService mailService) {
        this.topicService = topicService;
        this.subtopicService = subtopicService;
        this.mentorApplService = mentorApplService;
        this.userService = userService;
        this.mentorshipService = mentorshipService;
        this.acceptedMentorService = acceptedMentorService;
        this.mentorSolrService = mentorSolrService;
        this.mailService = mailService;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    public boolean adminChecker(){  // RETURNS FALSE IF THE LOGGED IN USER IS NOT ADMIN
        //UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMİNS"));
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    @GetMapping("/")
    public String index() {
        if(adminChecker()){
            return "admin/unauthorized";
        }
        //mailService.sendReminderMail("hasekisinemelif@gmail.com", "Spring", "JPA");
        return "user/user-home";
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    @GetMapping("/user-dashboard")
    public String userDashboard(Model model) {
        if(adminChecker()){
            return "admin/unauthorized";
        }
        ///////         AS A MENTOR         ////////
        RegularUser currentUser = userService.getCurrentUser();
        List<AcceptedMentor> currentUsersMentorships = acceptedMentorService.findAllByUser(currentUser);
        List<Mentee> mentoring = new ArrayList<>();
        for(AcceptedMentor a : currentUsersMentorships){
            List<Mentee> temp = mentorshipService.findAllByAcceptedmentor(a);
            for(Mentee m : temp){
                mentoring.add(m);
            }
        }
        model.addAttribute("mentoring", mentoring);
        ////////      AS A MENTEE          ////////
        List<Mentee> menteeing = mentorshipService.findAllByUser(currentUser);
        model.addAttribute("menteeing", menteeing);
        return "user/user-dashboard";
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    //          APPLY AS MENTOR        //
    @GetMapping("/apply-as-mentor")
    public String topicForm(Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        model.addAttribute("application", new MentorApplication());
        List<Topic> topics = topicService.getAll();
        List<Subtopic> subtopics = subtopicService.getAll();
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("topics", topics);
        model.addAttribute("subtopics", subtopics);
        return "user/mentor-application";
    }

    @PostMapping("/apply-as-mentor")
    public String submitApplication(Model model, @ModelAttribute MentorApplication mentorApplication){
        if(adminChecker()){
            return "admin/unauthorized";
        }

        List<MentorApplication> allApplicationsByUser = mentorApplService.findAllByUser(userService.getCurrentUser());
        List<AcceptedMentor> allAcceptedApplicationsByUser = acceptedMentorService.findAllByUser(userService.getCurrentUser());

        List<Long> subtopicIds = new ArrayList<>();
        for(Subtopic s : mentorApplication.getSubtopics()){
            boolean addFlag = true;
            if(s.getTopic().equals(mentorApplication.getTopic())){  // if topic and subtopic are compatible
                for(MentorApplication a : allApplicationsByUser){
                    if(a.getSubtopic().equals(s)){
                        addFlag = false;
                        break;
                    }
                }
                for(AcceptedMentor a : allAcceptedApplicationsByUser){
                    if(a.getSubtopic().equals(s)){
                        addFlag = false;
                        break;
                    }
                }
                if(addFlag)
                    subtopicIds.add(s.getId());
            }
        }
        mentorApplication.setUser(userService.getCurrentUser());
        mentorApplication.setEmail(userService.getCurrentUser().getEmail());
        mentorApplService.createAndSaveMentorAppl(mentorApplication.getTopic().getId(), subtopicIds, mentorApplication.getUser().getName(), mentorApplication.getDetails(), mentorApplication.getEmail());
        return "redirect:/user/previous-applications";
    }

    //     UPDATE APPLICATION    //
    @GetMapping("/edit-application/{id}")
    public String updateAppl(@PathVariable("id") Long applicationId, Model model) throws NotFoundException {
        if(adminChecker()){
            return "admin/unauthorized";
        }
        MentorApplication mentorApplication = mentorApplService.findById(applicationId);
        // if the application doesn't belong to the current user, prevent editing
        if(!mentorApplication.getUser().equals(userService.getCurrentUser()))
            return "user/unauthorized";

        model.addAttribute("appl", mentorApplication);
        return "user/update-application";
    }

    @PostMapping("/edit-application/{id}")
    public String saveAppl(@PathVariable("id") Long applicationId, @ModelAttribute MentorApplication mentorApplication) throws NotFoundException {
        if(adminChecker()){
            return "admin/unauthorized";
        }
        MentorApplication prevAppl = mentorApplService.findById(applicationId);

        // if the application doesn't belong to the current user, prevent editing
        if(!prevAppl.getUser().equals(userService.getCurrentUser()))
            return "user/unauthorized";

        prevAppl.setDetails(mentorApplication.getDetails());
        mentorApplService.updateApplication(prevAppl);
        return "redirect:/user/previous-applications";
    }
    //        DELETE APPLICATION     //
    @RequestMapping("/delete-application/{id}")
    public String deleteTopic(@PathVariable("id") Long applicationId, Model model) throws NotFoundException {
        if(adminChecker()){
            return "admin/unauthorized";
        }
        // if the application doesn't belong to the current user, prevent editing
        if(!mentorApplService.findById(applicationId).getUser().equals(userService.getCurrentUser()))
            return "user/unauthorized";

        mentorApplService.deleteById(applicationId);
        return "redirect:/user/previous-applications";
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    @RequestMapping("/previous-applications")
    public String prevApplications(Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        model.addAttribute("sentapplications",mentorApplService.findAllByUser(userService.getCurrentUser()));
        model.addAttribute("accepted",acceptedMentorService.findAllByUser(userService.getCurrentUser()));
        return "user/previous-applications";
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //
    //           SEARCH MENTOR            //
    @GetMapping("/search-mentor")
    public String searchForm(Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        model.addAttribute("mentorsearch", new MentorSearch());
        RegularUser currentUser = userService.getCurrentUser();
        // Disable the main topics that the current user is being mentored
        List<Mentee> mentorships = mentorshipService.findAllByUser(currentUser);
        List<Topic> disabledTopics = new ArrayList<>();
        List<Topic> activeTopics = topicService.getAll();
        List<Subtopic> subtopics = subtopicService.getAll();
        for(Mentee m : mentorships){
            //if that mentorship is not yet completed
            if(!m.getPhaseStatus().equals("Tamamlandı")){
                if(!disabledTopics.contains(m.getTopic())){
                    disabledTopics.add(m.getTopic());
                    activeTopics.remove(m.getTopic());
                }
                for(Subtopic s: subtopicService.getSubtopicsByTopicById(m.getTopic().getId())){
                    subtopics.remove(s);
                }
            }
        }


        model.addAttribute("activetopics", activeTopics);
        model.addAttribute("disabledtopics", disabledTopics);
        model.addAttribute("subtopics", subtopics);
        return "user/search-mentor";
    }

    @PostMapping("/search-mentor")
    public String searchPost(Model model, @ModelAttribute MentorSearch mentorSearch){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        List<AcceptedMentor> acceptedMentors = new ArrayList<>();
        for(AcceptedMentor a: acceptedMentorService.findAllByTopic(mentorSearch.getTopic())){
            List<Mentee> mentees = mentorshipService.findAllByAcceptedmentor(a);
            int counter = 0;
            for(Mentee m: mentees){
                if(!m.getPhaseStatus().equals("Tamamlandı")){
                    counter++;
                }
            }
            // if that mentor has less than 2 active mentees, add to available mentors list
            if(!a.getUser().equals(userService.getCurrentUser()) && counter < 2){
                acceptedMentors.add(a);
            }
        }
        for(Subtopic s: mentorSearch.getSubtopics()){
            for(AcceptedMentor a: acceptedMentorService.findAllBySubtopic(s)){
                List<Mentee> mentees = mentorshipService.findAllByAcceptedmentor(a);
                int counter = 0;
                for(Mentee m: mentees){
                    if(!m.getPhaseStatus().equals("Tamamlandı")){
                        counter++;
                    }
                }
                if(!acceptedMentors.contains(a) && !a.getUser().equals(userService.getCurrentUser()) && counter < 2){
                    acceptedMentors.add(a);
                }
            }
        }
        //           SOLR - FREE TEXT SEARCH      //
        if(!mentorSearch.getSearchtext().equals("")){
            List<Mentor> mentorTextResults = mentorSolrService.findAllByDetails(mentorSearch.getSearchtext());
            for(Mentor m: mentorTextResults){
                AcceptedMentor mentor = acceptedMentorService.findById(m.getMentorid());
                List<Mentee> mentees = mentorshipService.findAllByAcceptedmentor(acceptedMentorService.findById(m.getMentorid()));
                int counter = 0;
                for(Mentee mentee: mentees){
                    if(!mentee.getPhaseStatus().equals("Tamamlandı")){
                        counter++;
                    }
                }
                if(!acceptedMentorService.findById(m.getMentorid()).getUser().equals(userService.getCurrentUser()) && counter < 2){
                    acceptedMentors.add(acceptedMentorService.findById(m.getMentorid()));
                }
            }
        }

        // Disable mentors that are currently mentoring 2 people
        List<AcceptedMentor> toRemove = new ArrayList<>();
        if(acceptedMentors.size() > 0){
            for(AcceptedMentor a: acceptedMentors){
                RegularUser user = a.getUser();
                // find how many times this user has been accepted as mentor
                List<AcceptedMentor> thisUsersAccepted = acceptedMentorService.findAllByUser(user);
                if(thisUsersAccepted.size() >= 2){
                    int counter = 0;
                    for(AcceptedMentor thisUser : thisUsersAccepted){
                        List<Mentee> mentees = mentorshipService.findAllByAcceptedmentor(thisUser);
                        if(mentees != null){
                            for(Mentee m : mentees){
                                if(!m.getPhaseStatus().equals("Tamamlandı")){
                                    counter++;
                                }
                            }
                        }
                    }
                    if(counter >= 2){
                        for(AcceptedMentor thisUser: thisUsersAccepted){
                            if(acceptedMentors.contains(thisUser))
                                toRemove.add(thisUser);
                        }
                    }
                }
            }
        }

        acceptedMentors.removeAll(toRemove);

        // check if this user already has an active mentorship with one of the acceptedmentors
        List<Mentee> allByUser = mentorshipService.findAllByUser(userService.getCurrentUser());
        List<AcceptedMentor> thisUsersMentors = new ArrayList<>();
        for(Mentee m : allByUser){
            if(acceptedMentors.contains(m.getAcceptedMentor()) && !m.getPhaseStatus().equals("Tamamlandı")){
                acceptedMentors.remove(m.getAcceptedMentor());
            }
        }


        model.addAttribute("mentors",acceptedMentors);
        return "user/search-results";
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------- //

    //         CHOOSE MENTOR       //
    @RequestMapping("/choose-mentor/{mentorid}")
    public String chooseMentor(@PathVariable("mentorid") Long mentorId, Model model){
        if(adminChecker()){
            return "admin/unauthorized";
        }
        AcceptedMentor chosenMentor = acceptedMentorService.findById(mentorId);
        RegularUser currentUser = userService.getCurrentUser();
        mentorshipService.createAndSaveMentorship(chosenMentor.getTopic().getId(),chosenMentor.getSubtopic().getId(),chosenMentor.getId(),chosenMentor.getName(),chosenMentor.getEmail());
        return "redirect:/user/user-dashboard/";
    }


}
