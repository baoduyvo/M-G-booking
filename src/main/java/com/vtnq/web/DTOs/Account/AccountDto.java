package com.vtnq.web.DTOs.Account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

/**
 * DTO for {@link com.vtnq.web.Entities.Account}
 */
public class AccountDto{
    @NotBlank(message = "Full Name is required")
    private String fullName;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Address is required")
    private String address;


    private String otp;
    private String avatar;
    private String password;
    private String accountType;
    @Min(value = 1,message = "Country is required")
    private Integer countryId;
    private Instant createdOTP;

    public AccountDto() {
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName( String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Instant getCreatedOTP() {
        return createdOTP;
    }

    public void setCreatedOTP(Instant createdOTP) {
        this.createdOTP = createdOTP;
    }
}