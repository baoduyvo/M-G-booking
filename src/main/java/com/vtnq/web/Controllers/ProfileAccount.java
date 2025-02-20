package com.vtnq.web.Controllers;

import com.vtnq.web.DTOs.Account.UserAccountDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AuthService;
import com.vtnq.web.Service.CountryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"","/"})
public class ProfileAccount {
    @Autowired
    private AuthService authService;
    @Autowired
    private CountryService countryService;
    @RequestMapping("Profile")
    public String profile(ModelMap model, HttpServletRequest request) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");

            if(currentAccount==null){
                return "redirect:/Login";
            }
            model.put("Account",authService.GetAccountUser(currentAccount.getId()));
            model.put("Country",countryService.findAll());
            return "/User/Profile/Profile";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Profile")
    public String Profile(@ModelAttribute("Account")UserAccountDTO userAccountDTO, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            if(authService.UpdateProfileUser(userAccountDTO)) {
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Profile";
            }else{
                redirectAttributes.addFlashAttribute("message", "Profile update failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Profile";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
