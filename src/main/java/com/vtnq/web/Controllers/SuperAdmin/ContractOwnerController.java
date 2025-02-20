package com.vtnq.web.Controllers.SuperAdmin;

import com.vtnq.web.DTOs.ContractOwner.ContractOwnerDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.ContractOwnerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller("ContractOwnerSuperAdmin")
@RequestMapping({"/Admin"})
public class ContractOwnerController {
    @Autowired
    private ContractOwnerService contractOwnerService;

    @GetMapping("Contract")
    public String Contract(ModelMap model, HttpServletRequest request,@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "")String name) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            List<ContractOwnerDto> Contracts = contractOwnerService.findAll(currentAccount.getCountryId());
            List<ContractOwnerDto> filteredContracts = Contracts.stream().filter(city -> city.getOwnerName().contains(name)).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredContracts.size());
            List<ContractOwnerDto> paginatedOwnerDto = filteredContracts.subList(start, end);
            model.put("Contract",paginatedOwnerDto);
            model.put("totalPages", (int) Math.ceil((double) filteredContracts.size() / size));
            model.put("currentPage", page);
            return "/Admin/Contract/Contract";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
