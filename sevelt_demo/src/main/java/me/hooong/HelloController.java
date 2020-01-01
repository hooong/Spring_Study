package me.hooong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello, " + helloService.getName();
    }

    @GetMapping("/sample")
    public String sample() {
        // mv라는 변수로 문자열로 넘기면 view의 이름으로 인식
        // ResponceBody가 없다면.
        return "/WEB-INF/sample.jsp";
    }

}
