package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.ContractOwner.ContractOwnerDto;
import com.vtnq.web.Service.ContractOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api/ContractOwner")
public class ContractOwnerController {
    @Autowired
    private ContractOwnerService contractOwnerService;

    @PostMapping(value = "AcceptContract")
    public ResponseEntity<Object> AcceptContract(@RequestBody  ContractOwnerDto contractOwnerDto) {
        try {
            Map<String, Object> response = new LinkedHashMap<>();
            if(contractOwnerService.AcceptRegister(contractOwnerDto)) {
                response.put("status",200);
                response.put("message","Accept Contract Success");
                return ResponseEntity.ok(response);
            }else{
                response.put("status",400);
                response.put("message","Accept Contract Failed");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}
