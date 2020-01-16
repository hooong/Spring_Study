package me.hooong.demowebmvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.swing.tree.ExpandVetoException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SampleController {

    @GetMapping("/events/form")
    public String eventsForm(Model model) {
        model.addAttribute("event",new Event());
        return "/events/form";
    }

    @PostMapping("/events")
    public String createEvent(@Validated @ModelAttribute Event event,
                           BindingResult bindingResult,
                           Model model) {
        if(bindingResult.hasErrors()) {
            return "/events/form";
        }

        return "redirect:/events/list";
    }

    @GetMapping("/events/list")
    public String getEvents(Model model) {
        Event event1 = new Event();
        event1.setName("hooong");
        event1.setLimit(10);

        Event event2 = new Event();
        event2.setName("hooong2");
        event2.setLimit(20);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);

        model.addAttribute(eventList);

        return "/events/list";
    }
}
