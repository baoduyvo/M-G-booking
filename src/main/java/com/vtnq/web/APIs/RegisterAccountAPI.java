package com.vtnq.web.APIs;

import com.vtnq.web.Controllers.RegisterController;
import com.vtnq.web.DTOs.Account.RegisterUser;
import com.vtnq.web.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
@RestController
@RequestMapping({"/api/account"})
public class RegisterAccountAPI  {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "register/user")
    public ResponseEntity<Object> RegisterUser(@RequestBody RegisterUser registerUser) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (authService.RegisterAccount(registerUser)) {
                response.put("status", 200);
                response.put("message", "Register User Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", 400);
                response.put("message", "Register User Failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
