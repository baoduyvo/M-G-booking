package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AirportService;
import com.vtnq.web.Service.BookingService;
import com.vtnq.web.Service.CityService;
import com.vtnq.web.Service.FlightService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("AdminHomeController")
@RequestMapping({"/Admin"})
public class HomeController {
    @Autowired
    private CityService cityService;
    @Autowired
    private AirportService airportService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private FlightService flightService;
    @GetMapping("Home")
    public String Home(ModelMap model, HttpServletRequest request) {
        Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
        if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
            return "redirect:/LoginAdmin";
        }
        model.put("City",cityService.CountCity(currentAccount.getCountryId()));
        model.put("Airport",airportService.CountAirport(currentAccount.getCountryId()));
        model.put("booking",bookingService.CountBookings(currentAccount.getCountryId()));
        model.put("Flight",flightService.CountFlight(currentAccount.getCountryId()));
        return "Admin/Home/index";
    }
}
