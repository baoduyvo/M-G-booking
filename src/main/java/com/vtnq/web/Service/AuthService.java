package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Account.AccountDto;
import com.vtnq.web.DTOs.Account.AdminAccountList;
import com.vtnq.web.DTOs.Account.RegisterUser;
import com.vtnq.web.DTOs.Account.UserAccountDTO;
import com.vtnq.web.Entities.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AuthService extends UserDetailsService {
    public Account GetAccountByEmail(String Email);
    public boolean RegisterAdmin(AccountDto accountDto);
    public boolean RegisterAccount(RegisterUser accountDto);
    public boolean emailExists(String email);
    public boolean existPhone(String phone);
    public boolean existFullName(String fullName);
    public List<AdminAccountList>getAdmin();
    public boolean ForgetAccount(String email);
    public boolean CheckOTP(String email, String otp);
    public boolean ChangePassword(String email, String password);
    public UserAccountDTO GetAccountUser(int id);
    public boolean UpdateProfileUser(UserAccountDTO userAccountDTO);
    public boolean ResetPassword(String email);
    public boolean existAccountCountry(int countryId);
    public Account LoginMobile(String Email,String Password);
    public int CountAdmin();
}
