package me.hooong.demowebmvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@Controller
public class SampleController {

    @PostMapping("/events")
    @ResponseBody
    public Event getEvent(@RequestParam String name,
                          @RequestParam Integer limit) {
        Event event = new Event();
        event.setName(name);
        event.setLimit(limit);
        return event;
    }
}
