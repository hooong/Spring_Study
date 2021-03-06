package me.hooong.demowebmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

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
public class EventController {

//    @Autowired
//    EventValidator eventValidator;

//    @ExceptionHandler({EventException.class, RuntimeException.class})
//    public String eventErrorHandler(RuntimeException exception, Model model) {
//        model.addAttribute("message","event error");
//        return "error";
//    }



    @ExceptionHandler
    public String runtimeErrorHandler(RuntimeException exception, Model model) {
        model.addAttribute("message","runtime error");
        return "error";
    }



//    @ModelAttribute
//    public void categories(Model model) {
//        model.addAttribute("categories",List.of("study", "seminar", "hobby", "social"));
//    }



    @GetMapping("/events/form/name")
    public String eventsFormName(Model model) {
        throw new EventException();

//        model.addAttribute("event",new Event());
//        return "/events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventsFormNameSubmit(@Validated @ModelAttribute Event event,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/events/form-name";
        }
//        eventValidator.validate(event, bindingResult);

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
                                        SessionStatus sessionStatus,
                                        RedirectAttributes attributes) {
        if(bindingResult.hasErrors()) {
            return "/events/form-limit";
        }
        sessionStatus.setComplete();
        attributes.addFlashAttribute("newEvent",event);
        return "redirect:/events/list";
    }

    @GetMapping("/events/list")
    public String getEvents(Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);

        Event spring = new Event();
        spring.setName("hooong");
        spring.setLimit(10);

        Event newEvent = (Event) model.asMap().get("newEvent");

        List<Event> eventList = new ArrayList<>();
        eventList.add(spring);
        eventList.add(newEvent);

        model.addAttribute(eventList);

        return "/events/list";
    }
}
