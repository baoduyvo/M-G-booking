package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.DTOs.Airport.AirportDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Airport;
import com.vtnq.web.Service.AirportService;
import com.vtnq.web.Service.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller("AirportAdminController")
@RequestMapping({"/Admin"})
public class AirPortController {
    @Autowired
    private CityService cityService;
    @Autowired
    private AirportService airportService;
    @PostMapping("AirPort/add")
    public String add(@ModelAttribute("AirPort") AirportDto airportDto, RedirectAttributes redirectAttributes
    , BindingResult bindingResult,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(airportDto.getName()==null || airportDto.getName().isEmpty()){
                redirectAttributes.addFlashAttribute("message", "Name is required");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/add";
            }
            if(airportDto.getIdCity()==0 ){
                redirectAttributes.addFlashAttribute("message", "City is required");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/add";
            }
            if(airportService.existAirportInCity(airportDto.getIdCity())){
                redirectAttributes.addFlashAttribute("message","This city has an airport.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/add";
            }
            if(airportService.existByName(airportDto.getName())) {
                redirectAttributes.addFlashAttribute("message", "Airport already exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/add";
            }
            if(airportService.save(airportDto)) {
                redirectAttributes.addFlashAttribute("message", "Airport add Success");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/AirPort";
            }else{
                redirectAttributes.addFlashAttribute("message","Airport add Failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/add";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("AirPort/update")
    public String update(HttpServletRequest request,ModelMap model,@ModelAttribute("AirPort")AirportDto airportDto,
                         RedirectAttributes redirectAttributes,BindingResult bindingResult) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null && !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(airportDto.getName()==null || airportDto.getName().isEmpty()){
                redirectAttributes.addFlashAttribute("message", "Name is required");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/edit/"+airportDto.getId();
            }
            if(airportService.existByName(airportDto.getName())) {
                redirectAttributes.addFlashAttribute("message", "Airport already exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/AirPort/edit/"+airportDto.getId();
            }
           if(airportService.save(airportDto)) {
               redirectAttributes.addFlashAttribute("message", "Airport update Success");
               redirectAttributes.addFlashAttribute("messageType", "success");
               return "redirect:/Admin/AirPort";
           }else{
               redirectAttributes.addFlashAttribute("message","Airport update Failed");
               redirectAttributes.addFlashAttribute("messageType", "error");
               return "redirect:/Admin/AirPort/edit/"+airportDto.getId();
           }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("AirPort/add")
    public String add(HttpServletRequest request, ModelMap model) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null && !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("City",cityService.findCityAll(account.getCountryId()));
            model.put("AirPort",new AirportDto());
            return "Admin/AirPort/add";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("AirPort/edit/{id}")
    public String edit(@PathVariable int id,ModelMap model,HttpServletRequest request) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");

            if(currentAccount==null && !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("AirPort",airportService.findById(id));
            model.put("City",cityService.findCityAll(currentAccount.getCountryId()));
            return "Admin/AirPort/Edit";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("AirPort")
    public String airport(HttpServletRequest request, ModelMap model,@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String name) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");

            if(currentAccount==null && !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<Airport>Airports=airportService.findAll(currentAccount.getCountryId());
            List<Airport>filteredAirports=Airports.stream().filter(airport -> airport.getName().contains(name)).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredAirports.size());
            List<Airport>paginatedAirports=filteredAirports.subList(start, end);
            model.put("AirPort",paginatedAirports);
            model.put("totalPages", (int) Math.ceil((double) filteredAirports.size() / size));
            model.put("currentPage", page);
            return "Admin/AirPort/AirPort";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
