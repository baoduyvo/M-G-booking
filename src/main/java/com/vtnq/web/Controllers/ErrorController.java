package com.vtnq.web.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"","/"})
public class ErrorController {
    @GetMapping({"Error"})
    public String error() {
        try {
        return "User/404";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
