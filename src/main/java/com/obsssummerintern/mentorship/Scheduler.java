package com.obsssummerintern.mentorship;

import com.obsssummerintern.mentorship.domain.Mentee;
import com.obsssummerintern.mentorship.domain.Phase;
import com.obsssummerintern.mentorship.service.MailService;
import com.obsssummerintern.mentorship.service.MentorshipService;
import com.obsssummerintern.mentorship.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class Scheduler {
    private MentorshipService mentorshipService;
    private PhaseService phaseService;
    private MailService mailService;

    @Autowired
    public Scheduler(MentorshipService mentorshipService, PhaseService phaseService,MailService mailService){
        this.mentorshipService = mentorshipService;
        this.phaseService = phaseService;
        this.mailService = mailService;
    }

    //@Scheduled(cron = "*/30 * * * * *")
    @Scheduled(cron = "0 0 23 * * *")
    protected void endingPhases() throws Exception {
        List<Mentee> mentees = mentorshipService.findAll();
        Date today = new Date();

        for(Mentee m: mentees){
            // reminder mail
            if(!m.getPhaseStatus().equals("Tamamlandı") && !m.getPhaseStatus().equals("Başlamadı")){
                List<Phase> phases = phaseService.getPhasesByMenteeId(m.getId());
                for(Phase p: phases){
                    if(p.getStatus().equals("Aktif")){
                        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(p.getEndDate());

                        long diffInMillies = Math.abs(endDate.getTime() - today.getTime());
                        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        if(diff < 2){
                            mailService.sendReminderMail(m.getUser().getEmail(), m.getTopic().getTitle(), p.getTitle(), m.getAcceptedMentor().getName(),m.getSubtopic().getTitle());
                            mailService.sendReminderMail(m.getEmail(), m.getTopic().getTitle(), p.getTitle(), m.getUser().getName(),m.getSubtopic().getTitle());
                        }
                        break;
                    }
                }
            }

            // ending mail
            if(m.getPhaseStatus().equals("Tamamlandı")){
                List<Phase> phases = phaseService.getPhasesByMenteeId(m.getId());
                for(Phase p: phases){
                    // if it's the last phase and it was completed today, send a finishing mail
                    if(p.getPhaseNumber().equals(String.valueOf(phases.size()))){
                        Date endDate = new SimpleDateFormat("dd-MM-yyyy").parse(p.getEndDate());
                        long diffInMillies = Math.abs(endDate.getTime() - today.getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        if(diff < 1){
                            mailService.sendFinishingMail(m.getUser().getEmail(), m.getTopic().getTitle(), p.getTitle(), m.getAcceptedMentor().getName(),m.getSubtopic().getTitle());
                            mailService.sendFinishingMail(m.getEmail(), m.getTopic().getTitle(), p.getTitle(), m.getUser().getName(),m.getSubtopic().getTitle());
                        }
                        break;
                    }
                }
            }
        }
    }
}
