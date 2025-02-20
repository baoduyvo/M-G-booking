package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Hotel.HotelSearchDTO;
import com.vtnq.web.DTOs.Hotel.ShowDetailHotel;
import com.vtnq.web.Entities.Hotel;
import com.vtnq.web.Service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController("HotelApiController")
@RequestMapping({"api/hotel"})
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @PostMapping("UpdateMultipleImage/{id}")
    public ResponseEntity<Object> updateMultipleImage(@PathVariable int id, @RequestParam(value = "MultiImage", required = false) List<MultipartFile> MultiImage, ModelMap model, HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        try {
            Map<String,Object>response=new LinkedHashMap<>();
            if(hotelService.updateMultipleImages(id,MultiImage)){
                response.put("status",200);
                response.put("message","Update Image SuccessFully");
                return ResponseEntity.ok(response);
            }else{
                response.put("status",500);
                response.put("message","Update Image Failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @DeleteMapping(value = "DeletePictureImage/{id}")
    public ResponseEntity<Object>DeletePictureImage(@PathVariable int id) {
        try {
            Map<String,Object>response=new LinkedHashMap<>();
            if(hotelService.deleteImageHotel(id)){
                response.put("message","Deleted Image Successfully");
                response.put("status",200);
                return ResponseEntity.ok(response);
            }else{
                response.put("message","Failed to Delete Image");
                response.put("status",500);
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("SearchHotel")
    public ResponseEntity<List<HotelSearchDTO>>SearchHotel(@RequestParam int idCity ,@RequestParam int QuantityRoom){
        try {
            return new ResponseEntity<List<HotelSearchDTO>>(hotelService.SearchHotelsMobile(idCity,QuantityRoom),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<HotelSearchDTO>>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("GetDetailHotel")
    public ResponseEntity<ShowDetailHotel>GetNameById(@RequestParam int id){
        try {
            return new ResponseEntity<ShowDetailHotel>(hotelService.FindDetailHotel(id),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ShowDetailHotel>(HttpStatus.BAD_REQUEST);
        }
    }
}
