package com.vtnq.web.Controllers.SuperAdmin;

import com.vtnq.web.DTOs.Airline.AirlineDto;
import com.vtnq.web.DTOs.Airline.ListAirlineDto;
import com.vtnq.web.DTOs.Airline.UpdateAirlineDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AirlineService;
import com.vtnq.web.Service.CountryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller("AirlineSuperAdminController")
@RequestMapping({"/SuperAdmin"})
public class AirlineController {
    @Autowired
    private AirlineService airlineService;
    @Autowired
    private CountryService countryService;
    @PostMapping("/Airline/UpdateAirline")
    public String UpdateAirline(@ModelAttribute("Airline")UpdateAirlineDTO updateAirlineDTO, @RequestParam("imageForm")MultipartFile multipartFile, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null && !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors:\n");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format(" %s - %s\n", error.getField(), error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());

                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Airline/edit/"+updateAirlineDTO.getId();
            }
            if(airlineService.existName(updateAirlineDTO.getName())) {
                redirectAttributes.addFlashAttribute("message", "Name already exists!");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Airline/edit/"+updateAirlineDTO.getId();
            }
            if(airlineService.updateArline(updateAirlineDTO,multipartFile)) {
                redirectAttributes.addFlashAttribute("message", "Airline updated successfully!");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/SuperAdmin/Airline";
            }else{
                redirectAttributes.addFlashAttribute("message", "Error updating airline!");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Airline/edit/"+updateAirlineDTO.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Airline")
    public String Airline(ModelMap model,@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "")String name,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null && !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<ListAirlineDto> airlines=airlineService.findAll();
            List<ListAirlineDto>filteredAirlines=airlines.stream().filter(airline->
                    airline.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredAirlines.size());
            List<ListAirlineDto>paginatedAirline=filteredAirlines.subList(start, end);

            model.put("Airline",paginatedAirline);
            model.put("totalPages", (int) Math.ceil((double) filteredAirlines.size() / size));
            model.put("currentPage", page);
            return "SuperAdmin/Airline/Airline";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Airline/edit/{id}")
    public String edit(@PathVariable int id, ModelMap model,HttpServletRequest request) {
    try {
        Account account = (Account) request.getSession().getAttribute("currentAccount");
        if(account==null && !"ROLE_SUPERADMIN".equals(account.getAccountType())){
            return "redirect:/LoginAdmin";
        }
        model.put("Airline",airlineService.findAirlineById(id));
        model.put("Country",countryService.findAll());
        return "SuperAdmin/Airline/UpdateAirline";
    }catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
    @PostMapping("Airline/add")
    public String addAirline(@ModelAttribute("airline") @Valid AirlineDto airlineDto, BindingResult bindingResult, ModelMap model,
                             RedirectAttributes redirectAttributes,HttpServletRequest request) {
      try {
          Account account = (Account) request.getSession().getAttribute("currentAccount");
          if(account==null && !"ROLE_SUPERADMIN".equals(account.getAccountType())){
              return "redirect:/LoginAdmin";
          }
          if(bindingResult.hasErrors()) {
              StringBuilder errorMessages = new StringBuilder("Validation errors:\n");
              bindingResult.getFieldErrors().forEach(error ->
                      errorMessages.append(String.format(" %s - %s\n", error.getField(), error.getDefaultMessage()))
              );
              redirectAttributes.addFlashAttribute("message", errorMessages.toString());

              redirectAttributes.addFlashAttribute("messageType", "error");
              return "redirect:/SuperAdmin/Airline/add";
          }
          if(airlineService.existName(airlineDto.getName())) {
              redirectAttributes.addFlashAttribute("message", "Name already exists!");
              redirectAttributes.addFlashAttribute("messageType", "error");
              return "redirect:/SuperAdmin/Airline/add";
          }
          if(airlineService.addAirline(airlineDto)) {
              redirectAttributes.addFlashAttribute("message", "Airline added successfully!");
              redirectAttributes.addFlashAttribute("messageType", "success");
              return "redirect:/SuperAdmin/Airline";
          }else{
              redirectAttributes.addFlashAttribute("message", "Airline added failed!");
              redirectAttributes.addFlashAttribute("messageType", "error");
              return "redirect:/SuperAdmin/Airline/add";
          }
      }catch (Exception e) {
          e.printStackTrace();
          return null;
      }
    }
    @GetMapping("Airline/add")
    public String addAirline(ModelMap model,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("Airline",new AirlineDto());
            model.put("Country",countryService.findAll());
            return "SuperAdmin/Airline/add";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
