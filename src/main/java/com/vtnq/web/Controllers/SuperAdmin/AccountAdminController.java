package com.vtnq.web.Controllers.SuperAdmin;

import com.vtnq.web.DTOs.Account.AccountDto;
import com.vtnq.web.DTOs.Account.AdminAccountList;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AuthService;
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

@Controller("AccountAdminController")
@RequestMapping({"/SuperAdmin"})
public class AccountAdminController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private AuthService authService;
    @GetMapping("AccountAdmin/add")
    public String addAccountAdmin(ModelMap model, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("Country",countryService.findAll());
            model.put("Admin", new AccountDto());
            return "SuperAdmin/AccountAdmin/add";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("AccountAdmin")
    public String AccountAdmin(ModelMap model,@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "")String name,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<AdminAccountList>admin=authService.getAdmin();
            List<AdminAccountList>filteredAdmin=admin.stream().
                    filter(admins->admins.getFullName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            int start=(page-1)*size;
            int end=Math.min(page*size,filteredAdmin.size());
            List<AdminAccountList>PaginatedAdmin=filteredAdmin.subList(start,end);
            model.put("Admin",PaginatedAdmin);
            model.put("totalPages", (int) Math.ceil((double) filteredAdmin.size() / size));
            model.put("currentPage", page);
            return "SuperAdmin/AccountAdmin/AccountAdmin";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("AccountAdmin/add")
    public String addAccountAdmin(@ModelAttribute("Admin") @Valid AccountDto admin,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,HttpServletRequest request) {

        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_SUPERADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(result.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors:\n");
                result.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format(" %s - %s\n", error.getField(), error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());

                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }
            if(authService.emailExists(admin.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "Email is Already Exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }
            if(authService.existFullName(admin.getFullName())) {
                redirectAttributes.addFlashAttribute("message", "Full Name Already Exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }
            if(authService.existPhone(admin.getPhone())) {
                redirectAttributes.addFlashAttribute("message", "Phone Number Already Exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }
            if(authService.existAccountCountry(admin.getCountryId())){
                redirectAttributes.addFlashAttribute("message","This country is already managed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }
            if(authService.RegisterAdmin(admin)){
                redirectAttributes.addFlashAttribute("message", "Register Account Successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }else{
                redirectAttributes.addFlashAttribute("message", "Register Account Failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/SuperAdmin/AccountAdmin/add";
            }

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
