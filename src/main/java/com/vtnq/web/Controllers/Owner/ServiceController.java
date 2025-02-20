package com.vtnq.web.Controllers.Owner;

import com.vtnq.web.DTOs.Service.ServiceDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Service;
import com.vtnq.web.Service.ServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Owner")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @PostMapping("service/add")
    public String add(@ModelAttribute("service") @Valid ServiceDTO serviceDTO, BindingResult result, RedirectAttributes redirectAttributes,
                      HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            if (result.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                result.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/service/add";
            }
            if(serviceService.addService(serviceDTO)) {
                redirectAttributes.addFlashAttribute("message", "Service Added");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/service/add";
            }else{
                redirectAttributes.addFlashAttribute("message", "Service Not Added");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/service/add";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("service/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idHotel = (Integer) session.getAttribute("id");
            if(serviceService.deleteService(id)) {
                redirectAttributes.addFlashAttribute("message", "Delete Service Success");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/service/"+idHotel;
            }else{
                redirectAttributes.addFlashAttribute("message", "Delete Service Not Success");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/service/"+idHotel;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("service/update")
    public String update(@ModelAttribute("service") @Valid ServiceDTO serviceDTO,BindingResult result, RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            if (result.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                result.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/service/edit/"+serviceDTO.getId();
            }
            if(serviceService.addService(serviceDTO)) {
                redirectAttributes.addFlashAttribute("message", "Service Updated Successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/service/edit/"+serviceDTO.getId();
            }else{
                redirectAttributes.addFlashAttribute("message", "Service Not Updated Successfully");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/service/edit/"+serviceDTO.getId();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("service/edit/{id}")
    public String edit(@PathVariable("id")int id, ModelMap model,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            model.put("service", serviceService.findById(id));
            return "Owner/Service/edit";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("service/add")
    public String add(ModelMap model,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer id = (Integer) session.getAttribute("id");
            ServiceDTO serviceDTO = new ServiceDTO();
            if(id!=null){
                serviceDTO.setHotelId(id);
            }
            model.put("service",serviceDTO);
            return "Owner/Service/add";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("service/{id}")
    public String service(ModelMap model,HttpServletRequest request, @PathVariable int id, HttpSession session,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "")String name) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            session.setAttribute("id",id);
            List<Service>services=serviceService.findAll(id);
            List<Service>filteredServices=services.stream().filter(service ->
                    service.getName().toLowerCase().contains(name.toLowerCase())
                    ).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredServices.size());
            List<Service>paginatedServices=filteredServices.subList(start, end);
            model.put("service",paginatedServices);
            model.put("totalPages", (int) Math.ceil((double) filteredServices.size() / size));
            model.put("currentPage", page);
            return "Owner/Service/Service";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
