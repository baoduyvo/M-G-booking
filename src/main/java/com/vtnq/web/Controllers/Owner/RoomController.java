package com.vtnq.web.Controllers.Owner;

import com.vtnq.web.DTOs.Room.RoomDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Room;
import com.vtnq.web.Entities.Type;
import com.vtnq.web.Service.RoomService;
import com.vtnq.web.Service.TypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller("RoomOwnerController")
@RequestMapping({"/Owner"})
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private TypeService typeService;
    @GetMapping("Room/{id}")
    public String Room(@PathVariable int id, ModelMap model, HttpSession session,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "")String name,
                       HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            session.setAttribute("id", id);
            List<Room>rooms=roomService.findAll(id);
            List<Room>filteredRooms=rooms.stream().filter(room ->
                    room.getType().getName().toLowerCase().contains(name.toLowerCase())
                    ).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredRooms.size());
            List<Room>paginatedRooms=filteredRooms.subList(start, end);
            model.put("room",paginatedRooms);
            model.put("totalPages", (int) Math.ceil((double) filteredRooms.size() / size));
            model.put("currentPage", page);
            model.put("id",id);
            return "Owner/Room/Room";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Room/add")
    public String add(@RequestParam List<Integer>roomTypes, @RequestParam List<Integer>roomCapacities, @RequestParam int IdHotel,
                      @RequestParam(value = "roomImages", required = false) List<MultipartFile> roomImages , RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            if (roomImages != null && !roomImages.isEmpty()) {
                // You can further process images if needed
                List<List<MultipartFile>> imagePaths = new ArrayList<>();
                for (MultipartFile file : roomImages) {
                    imagePaths.add(Collections.singletonList(file)); // Wrap each image in a list for further processing
                }

                // Call service to add room
                if (roomService.addRoom(roomTypes, roomCapacities, IdHotel, imagePaths)) {
                    redirectAttributes.addFlashAttribute("message", "Room added successfully");
                    redirectAttributes.addFlashAttribute("messageType", "success");
                    return "redirect:/Owner/Room/add";
                } else {
                    redirectAttributes.addFlashAttribute("message", "Room add failed");
                    redirectAttributes.addFlashAttribute("messageType", "error");
                    return "redirect:/Owner/Room/add";
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "No images uploaded");
                redirectAttributes.addFlashAttribute("messageType", "warning");
                return "redirect:/Owner/Room/add";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Room/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idHotel=(Integer) session.getAttribute("id");
            if(roomService.delete(id)){
                redirectAttributes.addFlashAttribute("message", "Room deleted successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/Room/"+idHotel;
            }else{
                redirectAttributes.addFlashAttribute("message", "Room deleted failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/Room/"+idHotel;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Room/update")
    public String update(@ModelAttribute("room")@Valid RoomDTO roomDTO, BindingResult result, RedirectAttributes redirectAttributes,HttpServletRequest request) {
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
                return "redirect:/Owner/Room/edit/"+roomDTO.getId();
            }
            if(roomService.update(roomDTO)){
                redirectAttributes.addFlashAttribute("message", "Room updated successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/Room/edit/"+roomDTO.getId();
            }else{
                redirectAttributes.addFlashAttribute("message", "Room updated failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/Room/edit/"+roomDTO.getId();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Room/edit/{id}")
    public String edit(@PathVariable int id, ModelMap model, HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idHotel=(Integer) session.getAttribute("id");
            model.put("typeRoom",typeService.findByHotel(idHotel));
            model.put("room",roomService.findById(id));
            model.put("picture",roomService.FindPictureByRoomId(id));
            return "Owner/Room/edit";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Room/addType")
    public String addType(@ModelAttribute("type") Type type, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            if(typeService.addType(type)){
                redirectAttributes.addFlashAttribute("message", "Type added successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Owner/Room/add";
            }else{
                redirectAttributes.addFlashAttribute("message", "Type add failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Owner/Room/add";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Room/add")
    public String add(ModelMap modelMap,HttpSession session,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer id=(Integer) session.getAttribute("id");
            Type type=new Type();
            type.setHotelId(id);
            modelMap.put("type",type);
            modelMap.put("id",id);
            return "Owner/Room/add";

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
