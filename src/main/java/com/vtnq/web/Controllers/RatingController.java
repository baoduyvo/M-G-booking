package com.vtnq.web.Controllers;

import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Rating;
import com.vtnq.web.Service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"","/"})
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @PostMapping("rating")
    public String rating(@ModelAttribute("Rating") Rating rating, ModelMap model,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Account account=(Account)request.getSession().getAttribute("currentAccount");
            if(!ratingService.existBookingRating(rating.getHotel().getId(),account.getId())){
                redirectAttributes.addFlashAttribute("message", "You have not booked this hotel");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/DetailHotel/"+rating.getHotel().getId();
            }
            if(ratingService.addRating(rating)) {
                redirectAttributes.addFlashAttribute("message", "Rating added Successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/DetailHotel/"+rating.getHotel().getId();
            }else{
                redirectAttributes.addFlashAttribute("message", "Rating not added Successfully");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/DetailHotel/"+rating.getHotel().getId();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
