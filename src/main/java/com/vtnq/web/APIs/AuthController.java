package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Account.ChangePassword;
import com.vtnq.web.DTOs.Account.CheckOTP;
import com.vtnq.web.DTOs.Account.ForgetDTO;
import com.vtnq.web.Service.AuthService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController("authControllerAPI")
@RequestMapping("api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "ForgetPassword")
    public ResponseEntity<Object> ForgetPassword(@RequestBody ForgetDTO forgetDTO) {
        try {
            Map<String, Object> response = new LinkedHashMap<>();
            if (authService.ForgetAccount(forgetDTO.getEmail())) {
                response.put("status", 200);
                response.put("message", "Forget Account Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", 400);
                response.put("message", "Forget Account Failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "CheckOTP")
    public ResponseEntity<Object> CheckOTP(@RequestBody CheckOTP checkOTP) {
        try {
            Map<String, Object> response = new LinkedHashMap<>();
            if (authService.CheckOTP(checkOTP.getEmail(), checkOTP.getOtp())) {
                response.put("status", 200);
                response.put("message", "Check OTP Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", 400);
                response.put("message", "Check OTP Failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "ChangePassword")
    public ResponseEntity<Object> ChangePassword(@RequestBody ChangePassword changePassword) {
        try {
            Map<String, Object> response = new LinkedHashMap<>();
            if (authService.ChangePassword(changePassword.getEmail(), changePassword.getPassword())) {
                response.put("status", 200);
                response.put("message", "Change Password Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", 400);
                response.put("message", "Change Password Failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}
