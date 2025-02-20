package com.vtnq.web.Controllers;

import com.vtnq.web.Entities.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("AboutController")
@RequestMapping({"","/"})
public class AboutController {
    @GetMapping("About")
    public String About(HttpServletRequest request) {
        try {
            Account account=(Account)request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_USER".equals(account.getAccountType())) {
                return "User/AboutUs/AboutUs";
            }else{
                return "User/AboutUs/AboutUsLogin";
            }


        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
