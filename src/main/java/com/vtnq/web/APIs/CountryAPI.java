package com.vtnq.web.APIs;

import com.vtnq.web.Service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
@RestController
@RequestMapping("api/Country")
public class CountryAPI {
    @Autowired
    private CountryService countryService;

    @GetMapping("/All")
    public ResponseEntity<Object> getAllCountry() {
        Map<String,Object> response=new LinkedHashMap<>();
        try {
            if(countryService.findAll().size()>0){
                response.put("status",200);
                response.put("message","Success");
                response.put("data",countryService.findAll());
                return ResponseEntity.ok(response);
            }else{
                response.put("status",400);
                response.put("message","Fail");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
