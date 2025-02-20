package com.vtnq.web.Controllers.SuperAdmin;

import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AirlineService;
import com.vtnq.web.Service.AuthService;
import com.vtnq.web.Service.CountryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller("superAdminHomeController")
@RequestMapping({"/SuperAdmin"})
public class HomeController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AirlineService airlineService;
    @GetMapping("Home")
    public String Home(HttpServletRequest request, ModelMap model) {
        Account account = (Account) request.getSession().getAttribute("currentAccount");
        if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
            return "redirect:/LoginAdmin";
        }
        model.put("account", authService.CountAdmin());
        model.put("airline", airlineService.CountAirline());
        model.put("country", countryService.CountCountry());
        return "SuperAdmin/Home/index";
    }

}
