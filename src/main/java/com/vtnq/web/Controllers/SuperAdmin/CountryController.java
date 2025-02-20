package com.vtnq.web.Controllers.SuperAdmin;

import com.vtnq.web.DTOs.CountryDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Country;
import com.vtnq.web.Service.CountryService;
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

@Controller("CountryHomeController")
@RequestMapping({"/SuperAdmin"})
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping("Country/add")
    public String AddCountry(ModelMap model,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("Country", new Country());
            return "SuperAdmin/Country/add";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @PostMapping("Country/update")
    public String update(@ModelAttribute("Country") Country country, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if (country.getName() == null || country.getName().isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Name Country is required");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Country/" + country.getId();
            }
            if (countryService.existCountry(country.getName())) {
                redirectAttributes.addFlashAttribute("message", "Name Country already exist");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Country/" + country.getId();
            }
            if (countryService.UpdateCountry(country)) {
                redirectAttributes.addFlashAttribute("message", "Update Country successful");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/SuperAdmin/Country";
            } else {
                redirectAttributes.addFlashAttribute("message", "Update Country failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/Country/" + country.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("Country/{id}")
    public String EditCountry(@PathVariable int id, ModelMap model, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("Country", countryService.findCountryById(id));
            return "SuperAdmin/Country/UpdateCountry";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("Country")
    public String Country(ModelMap model, @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String name, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<Country> countries = countryService.findAll();
            List<Country> filteredCountries = countries.stream()
                    .filter(country -> country.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toUnmodifiableList());
            int start = (page - 1) * size;
            int end = Math.min(page * size, filteredCountries.size());
            List<Country> paginatedCountries = filteredCountries.subList(start, end);
            model.put("Country", paginatedCountries);
            model.put("totalPages", (int) Math.ceil((double) filteredCountries.size() / size));
            model.put("currentPage", page);
            return "SuperAdmin/Country/Country";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("Country/add")
    public String AddCountry(@ModelAttribute("Country") @Valid CountryDto country, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Account account = (Account) request.getSession().getAttribute("currentAccount");
        if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
            return "redirect:/LoginAdmin";
        }
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("Validation errors: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
            );
            redirectAttributes.addFlashAttribute("message", errorMessages.toString());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/SuperAdmin/Country/add";
        }
        if (countryService.existCountry(country.getName())) {
            redirectAttributes.addFlashAttribute("message", "Name Country already exist");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/SuperAdmin/Country/add";
        }
        if (countryService.addCountry(country)) {
            redirectAttributes.addFlashAttribute("message", "Country Added Successfully");

            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/SuperAdmin/Country";
        } else {
            redirectAttributes.addFlashAttribute("message", "Country with this name already exists or an error occurred");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/SuperAdmin/Country/add";
        }

    }
}
