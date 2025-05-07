package gg.scala.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contactPage() {
        return "redirect:https://studios.scala.gg/contact";
    }
} 