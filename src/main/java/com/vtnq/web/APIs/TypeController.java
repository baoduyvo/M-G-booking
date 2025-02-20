package com.vtnq.web.APIs;

import com.vtnq.web.Entities.Type;
import com.vtnq.web.Service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("TypeApiController")
@RequestMapping({"api/type"})
public class TypeController {
    @Autowired
    private TypeService typeService;
    @GetMapping(value = "FindTypeByHotel/{id}")
    public ResponseEntity<List<Type>> FindTypeByHotel(@PathVariable int id) {
        try {
            return new ResponseEntity<List<Type>>(typeService.findByHotel(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<Type>>(HttpStatus.BAD_REQUEST);
        }
    }
}
