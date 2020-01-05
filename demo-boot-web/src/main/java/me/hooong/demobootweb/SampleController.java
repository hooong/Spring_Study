package me.hooong.demobootweb;

import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {

    // preHandler 1
    // preHandler 2
    // 요청 처리
    // postHandler 2
    // postHandler 1
    // 뷰 랜더링
    // afterCompletion 2
    // afterCompletion 1

    @GetMapping("/hello")
    public String hello(@RequestParam("id") Person person) {
        return "hello " + person.getName();
    }

    @GetMapping("/message")
    public String message(@RequestBody String body) {
        return body;
    }

    @GetMapping("/jsonMessage")
    public Person jsonMessage(@RequestBody Person person) {
        return person;
    }

}
