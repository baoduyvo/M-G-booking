package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.DTOs.District.DistrictDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.DistrictService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("DistrictAdminHomeController")
@RequestMapping({"/Admin"})
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping("District/edit/{id}")
    public String edit(@PathVariable int id, ModelMap model, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("district", districtService.findDistrictById(id));
            return "Admin/District/edit";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("District/add")
    public String add(ModelMap model, HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            Integer id = (Integer) session.getAttribute("id");
            DistrictDto districtDto = new DistrictDto();
            if (id != null) {
                districtDto.setIdCity(id);
            }
            model.put("district", districtDto);
            return "Admin/District/add";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("District/delete/{id}")
    public String delete(@PathVariable int id,RedirectAttributes redirectAttributes,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(districtService.delete(id)){
                Integer idcity = (Integer) session.getAttribute("id");
                redirectAttributes.addFlashAttribute("message", "Delete District Successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/District/" +idcity;
            }else{
                redirectAttributes.addFlashAttribute("message", "Delete District Failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/District/";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("District/update")
    public String update(@ModelAttribute("district") @Valid DistrictDto districtDto, BindingResult result, RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(result.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                result.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/District/edit/" + districtDto.getId();
            }
            if(districtService.addDistrict(districtDto)) {
                redirectAttributes.addFlashAttribute("message", "District updated successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/District/"+districtDto.getIdCity();
            }else{
                redirectAttributes.addFlashAttribute("message", "District updated fail");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/District/edit/" + districtDto.getIdCity();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("District/add")
    public String add(@ModelAttribute("district") @Valid DistrictDto districtDto, BindingResult bindingResult, HttpSession session
            , RedirectAttributes redirectAttributes,HttpServletRequest request) {
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
                return "redirect:/Admin/District/add";
            }
            if (districtService.existName(districtDto.getName())) {
                redirectAttributes.addFlashAttribute("message", "Name already exists");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/District/add";
            }
            if (districtService.addDistrict(districtDto)) {
                redirectAttributes.addFlashAttribute("message", "District added successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/District/" + districtDto.getIdCity();
            } else {
                redirectAttributes.addFlashAttribute("message", "District add failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/District/add";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("District/{id}")
    public String add(ModelMap model, @PathVariable int id, HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            session.setAttribute("id", id);
            model.put("District", districtService.findDistrict(id));
            return "Admin/District/District";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
