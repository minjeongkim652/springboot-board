package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  //어노테이션
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";  // /<- 이거의 주소에 의해서 index.html을 보여줘라
    }
}
