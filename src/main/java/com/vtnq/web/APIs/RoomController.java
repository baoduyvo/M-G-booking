package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Hotel.ShowDetailHotel;
import com.vtnq.web.DTOs.Room.RoomDetailHotel;
import com.vtnq.web.Entities.Picture;
import com.vtnq.web.Service.AmenitiesService;
import com.vtnq.web.Service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController("RoomApiController")
@RequestMapping({"api/room"})
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private AmenitiesService amenitiesService;
    @GetMapping("picture/{id}")
    public ResponseEntity<List<Picture>>getPicture(@PathVariable int id){
        try {
            return new ResponseEntity<List<Picture>>(amenitiesService.FindPictureByRoom(id), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<List<Picture>>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("UpdateMultipleImage/{id}")
    public ResponseEntity<Object> updateMultipleImage(@PathVariable int id, @RequestParam(value = "MultiImage", required = false) List<MultipartFile> MultiImage, ModelMap model, HttpServletRequest request,
                                                      RedirectAttributes redirectAttributes) {
        try {
            Map<String,Object> response=new LinkedHashMap<>();
            if(roomService.UpdateMultipleImages(id,MultiImage)){
                response.put("status",200);
                response.put("message","Update Image SuccessFully");
                return ResponseEntity.ok(response);
            }else{
                response.put("status",500);
                response.put("message","Update Image Failed");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "DeletePictureImage/{id}")
    public ResponseEntity<Object>DeletePictureImage(@PathVariable int id){
        try {
            Map<String,Object>response=new LinkedHashMap<>();
            if(roomService.deleteMultipleImagesRoom(id)){
                response.put("status",200);
                response.put("message","Delete Image Successfully");
                return ResponseEntity.ok(response);
            }else{
                response.put("status",500);
                response.put("message","Delete Image Failed");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("GetRoomByHotel")
    public ResponseEntity<List<RoomDetailHotel>> getRoomByHotel(@RequestParam int hotelId){
        try {
            return new ResponseEntity<List<RoomDetailHotel>>(roomService.ShowDetailHotel(hotelId),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
