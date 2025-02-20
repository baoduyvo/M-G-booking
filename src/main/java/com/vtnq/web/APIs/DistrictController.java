package com.vtnq.web.APIs;

import com.vtnq.web.Entities.District;
import com.vtnq.web.Service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("DistrictHomeController")
@RequestMapping("api/District")
public class DistrictController {
    @Autowired
    private DistrictService districtService;
    @GetMapping("{id}")
    public ResponseEntity<List<District>> getDistrict(@PathVariable int id) {
        try {
        return new ResponseEntity<List<District>>(districtService.findDistrict(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<District>>(HttpStatus.BAD_REQUEST);
        }
    }
}
