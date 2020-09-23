package com.obsssummerintern.mentorship.controller;

import com.obsssummerintern.mentorship.domain.PostPhase;
import com.obsssummerintern.mentorship.domain.Rating;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/postphase")
public class PostPhaseController {
    private PostPhase postPhase;

    @GetMapping
    public String showRating(Model model){
        model.addAttribute("rating", new Rating());
        return "rating";
    }
    @PostMapping
    public String save(Rating rating, String feedback, Model model){
        model.addAttribute("rating", rating);
        return "saved-rating";
    }
}
