package me.hooong.demowebmvc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SampleController {

    @GetHelloMapping
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @GetMapping("/events")
    @ResponseBody
    public String events() {
        return "events";
    }

    @GetMapping("/events/{id}")
    @ResponseBody
    public String getAnEvent(@PathVariable int id) {
        return "event";
    }

    @DeleteMapping("/events/{id}")
    @ResponseBody
    public String deleteEvent(@PathVariable String id) {
        return "event";
    }

}
