package me.hooong.demowebmvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.swing.tree.ExpandVetoException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("event")
public class SampleController {

    @GetMapping("/events/form/name")
    public String eventsFormName(Model model) {
        model.addAttribute("event",new Event());
        return "/events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventsFormNameSubmit(@Validated @ModelAttribute Event event,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/events/form-name";
        }
        return "redirect:/events/form/limit";
    }

    @GetMapping("/events/form/limit")
    public String eventsFormLimit(@ModelAttribute Event event, Model model) {
        model.addAttribute("event",event);
        return "/events/form-limit";
    }

    @PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                       BindingResult bindingResult,
                                       SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()) {
            return "/events/form-limit";
        }
        sessionStatus.setComplete();
        return "redirect:/events/list";
    }

    @GetMapping("/events/list")
    public String getEvents(Model model, @SessionAttribute LocalDateTime visitTime
            , HttpSession httpSession) {
        System.out.println(visitTime);
        LocalDateTime visitTime = (LocalDateTime) httpSession.getAttribute("visitTime");
        Event event1 = new Event();
        event1.setName("hooong");
        event1.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);

        model.addAttribute(eventList);

        return "/events/list";
    }
}
