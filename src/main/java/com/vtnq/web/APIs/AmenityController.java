package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;
import com.vtnq.web.Service.AmenitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("AmenityControllerApi")
@RequestMapping("api")
public class AmenityController {
    @Autowired
    private AmenitiesService amenitiesService;
    @GetMapping("Amenity/{id}")
    public ResponseEntity<List<AmenitiesList>> Amenity(@PathVariable int id) {
        try {
        return new ResponseEntity<List<AmenitiesList>>(amenitiesService.FindAmenitiesByRoom(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<AmenitiesList>>(HttpStatus.BAD_REQUEST);
        }
    }

}
