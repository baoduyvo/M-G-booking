package com.vtnq.web.Controllers;

import com.vtnq.web.Entities.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("ContactController")
@RequestMapping({"","/"})
public class ContactController {
    @GetMapping("Contact")
    public String contact(HttpServletRequest request){
    try {
        Account account=(Account) request.getSession().getAttribute("currentAccount");
        if(account==null || !"ROLE_USER".equals(account.getAccountType())){
            return "User/Contact/Contact";
        }else{
            return "User/Contact/ContactLogin";
        }

    }catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
}
