package com.vtnq.web.Controllers;

import com.vtnq.web.DTOs.Account.ForgetDTO;
import com.vtnq.web.DTOs.LoginDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"","/"})
public class AuthController {
    @Autowired
    private AuthService authService;
    @GetMapping("ForgotPassword")
    public String ForgotPassword(ModelMap model, HttpServletRequest request) {
        Account account=(Account)request.getSession().getAttribute("currentAccount");
        if(account==null || "ROLE_USER".equals(account.getAccountType())){
            return "User/Forgot/ForgotPassword";
        }else{
            return "User/Forgot/ForgotPasswordAccount";
        }

    }
    @GetMapping("ForgotAdminPassword")
    public String ForgotAdminPassword(ModelMap model, HttpServletRequest request) {
        try {
            model.put("Forgot",new ForgetDTO());
            return "User/Forgot/ForgotAdmin";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("ForgotAdminPassword")
    public String ForgotAdminPassword(@ModelAttribute("Forgot")ForgetDTO forgetDTO, ModelMap model) {
        try {
            if(authService.ResetPassword(forgetDTO.getEmail())){
                return "redirect:/LoginAdmin";
            }else{
                return "redirect:/ForgotAdminPassword";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("LoginAdmin")
    public String LoginAdmin(ModelMap model,HttpServletRequest request,HttpSession session) {
        String message = (String)request.getSession().getAttribute("Message");
        String messageType = (String)request.getSession().getAttribute("MessageType");
        if(message!=null && messageType!=null){
            model.put("message",message);
            model.put("messageType",messageType);
            session.removeAttribute("Message");
            session.removeAttribute("MessageType");
        }
        model.put("login", new LoginDTO());
        return "SuperAdmin/Login/Login";

    }
    @GetMapping("Login")
    public String Login(ModelMap model, HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        String message = (String)request.getSession().getAttribute("Message");
        String messageType = (String)request.getSession().getAttribute("MessageType");
        Account account = (Account) request.getSession().getAttribute("currentAccount");


        if (account != null &&
                ("ROLE_USER".equals(account.getAccountType()) || "ROLE_OWNER".equals(account.getAccountType()))) {
            if (message != null && messageType != null) {
                redirectAttributes.addFlashAttribute("message", message);
                redirectAttributes.addFlashAttribute("messageType", messageType);


                session.removeAttribute("Message");
                session.removeAttribute("MessageType");
            }
            model.put("login", new LoginDTO());
            return "User/login/loginAccount";
        }


        if (message != null && messageType != null) {
           model.put("message",message);

            model.put("messageType", "error");
            session.removeAttribute("Message");
            session.removeAttribute("MessageType");

        }


        model.put("login", new LoginDTO());
        return "User/login/login";
    }

}
