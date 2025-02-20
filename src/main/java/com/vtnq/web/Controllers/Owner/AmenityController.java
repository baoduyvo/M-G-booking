package com.vtnq.web.Controllers.Owner;

import com.vtnq.web.DTOs.AmenityDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Amenity;
import com.vtnq.web.Service.AmenitiesService;
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

@Controller("AmenitiesOwnerController")
@RequestMapping({"/Owner"})
public class AmenityController {
    @Autowired
    private AmenitiesService amenitiesService;
    @GetMapping("Amenities/{id}")
    public String Amenities(@PathVariable int id, ModelMap model, HttpSession session, @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "")String name, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            session.setAttribute("id", id);
            List<Amenity>amenities=amenitiesService.findAll(id);
                List<Amenity>filteredAmenities=amenities.stream().filter(hotels->
                    hotels.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredAmenities.size());
            List<Amenity>paginatedAmenities=filteredAmenities.subList(start, end);
            model.put("amenity",paginatedAmenities);
            model.put("totalPages", (int) Math.ceil((double) filteredAmenities.size() / size));
            model.put("currentPage", page);
            model.put("id",id);
            return "Owner/Amenities/Amenities";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Amenities/delete/{id}")
    public String delete(@PathVariable int id, ModelMap model,RedirectAttributes redirectAttributes,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idHotel=(Integer)session.getAttribute("id");
            if(amenitiesService.delete(id)){
                redirectAttributes.addFlashAttribute("message","Amenity deleted successfully");
                redirectAttributes.addFlashAttribute("messageType","success");
                return "redirect:/Owner/Amenities/"+idHotel;
            }else{
                redirectAttributes.addFlashAttribute("message","Amenity not deleted successfully");
                redirectAttributes.addFlashAttribute("messageType","error");
                return "redirect:/Owner/Amenities/"+idHotel;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Amenities/update")
    public String update(@ModelAttribute("amenity") @Valid AmenityDto amenityDto,BindingResult result, RedirectAttributes redirectAttributes,HttpServletRequest request) {
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
                return "redirect:/Owner/Amenities/edit/" + amenityDto.getId();
            }
            if(amenitiesService.update(amenityDto)) {
                redirectAttributes.addFlashAttribute("message", "Amenity updated successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/Amenities/edit/" + amenityDto.getId();
            }else{
                redirectAttributes.addFlashAttribute("message", "Amenity update failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/Amenities/edit/" + amenityDto.getId();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Amenities/edit/{id}")
    public String edit(@PathVariable int id, ModelMap model, HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idHotel = (Integer) session.getAttribute("id");
            model.put("amenity",amenitiesService.findById(id));
            model.put("idHotel",idHotel);
            return "Owner/Amenities/edit";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Amenities/add")
    public String add(@ModelAttribute("amenity")@Valid AmenityDto amenity, BindingResult result, ModelMap model, RedirectAttributes redirectAttributes,
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
                return "redirect:/Owner/Amenities/add";
            }
            if(amenitiesService.addAmenity(amenity)) {
                redirectAttributes.addFlashAttribute("message", "Amenity added successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/Amenities/add";
            }else{
                redirectAttributes.addFlashAttribute("message", "Amenity not added successfully");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/Amenities/add";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Amenities/add")
    public String AmenitiesAdd(ModelMap model, HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer id = (Integer) session.getAttribute("id");
            AmenityDto amenityDto = new AmenityDto();
            amenityDto.setRoom_id(id);
            model.put("amenity", amenityDto);
            return "Owner/Amenities/add";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
