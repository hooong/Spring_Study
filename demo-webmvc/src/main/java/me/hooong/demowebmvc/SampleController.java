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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
//@SessionAttributes("event")
public class SampleController {

    @GetMapping("/events/form")
    public String eventsForm(Model model) {
        Event newEvent = new Event();
        newEvent.setLimit(50);
        model.addAttribute("event",newEvent);
        return "/events/form";
    }

    @PostMapping("/events")
    public String createEvent(@Validated @ModelAttribute Event event,
                              BindingResult bindingResult,
                              SessionStatus sessionStatus) {
        if(bindingResult.hasErrors()) {
            return "/events/form";
        }

        // form을 처리하는 곳에서 session을 끝낼 수 있다.
        sessionStatus.setComplete();
        return "redirect:/events/list";
    }

    @GetMapping("/events/list")
    public String getEvents(Model model) {
        Event event1 = new Event();
        event1.setName("hooong");
        event1.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);

        model.addAttribute(eventList);

        return "/events/list";
    }
}
