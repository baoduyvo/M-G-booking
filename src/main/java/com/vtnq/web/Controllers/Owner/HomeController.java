package com.vtnq.web.Controllers.Owner;

import com.vtnq.web.Entities.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("OwnerHomeController")
@RequestMapping("/Owner")
public class HomeController {
    @GetMapping({"","/"})
    public String Home(HttpServletRequest request)
    {
        Account account = (Account) request.getSession().getAttribute("currentAccount");
        if(account==null){
            return "redirect:/Login";
        }
        return "Owner/Home/Home";

    }
}
