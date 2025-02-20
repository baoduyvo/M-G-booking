package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.DTOs.City.CityDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.City;
import com.vtnq.web.Service.CityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller("CityAdminHomeController")
@RequestMapping({"/Admin"})
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping("City/add")
    public String addCity(ModelMap model, HttpServletRequest request) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }

            CityDto cityDto = new CityDto();
            cityDto.setCountryId(currentAccount.getCountryId());
            model.put("city", cityDto);
            return "Admin/City/add";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("City")
    public String City(ModelMap model, HttpServletRequest request, @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String name) {
        try {

            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<City> cities = cityService.findCityAll(currentAccount.getCountryId());
            List<City> filteredCities = cities.stream().filter(city -> city.getName().contains(name)).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredCities.size());
            List<City> paginatedCities = filteredCities.subList(start, end);
            model.put("City", paginatedCities);
            model.put("totalPages", (int) Math.ceil((double) filteredCities.size() / size));
            model.put("currentPage", page);
            return "Admin/City/City";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("City/UpdateCity")
    public String UpdateCity(@ModelAttribute("City")@Valid CityDto cityDto, ModelMap model,BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ",  error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/edit/"+cityDto.getId();
            }
            if(cityService.existName(cityDto.getName())) {
                redirectAttributes.addFlashAttribute("message", "City already exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/edit/"+cityDto.getId();
            }
            if(cityService.addCity(cityDto)) {
                redirectAttributes.addFlashAttribute("message", "City updated Successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/City";
            }else{
                redirectAttributes.addFlashAttribute("message", "City updated Failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/edit/"+cityDto.getId();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    @GetMapping("City/edit/{id}")
    public String editCity(ModelMap model, @PathVariable int id,HttpServletRequest request,RedirectAttributes redirectAttributes) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("City", cityService.findCityById(id));
            return "Admin/City/Detail";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("City/add")
    public String addCity(@ModelAttribute("city") @Valid CityDto cityDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/add";
            }
            if(cityService.existName(cityDto.getName())) {
                redirectAttributes.addFlashAttribute("message", "City already exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/add";
            }
            if (cityService.addCity(cityDto)) {
                redirectAttributes.addFlashAttribute("message", "Add City Success");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/City";
            } else {
                redirectAttributes.addFlashAttribute("message", "Add City Failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/City/add";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
