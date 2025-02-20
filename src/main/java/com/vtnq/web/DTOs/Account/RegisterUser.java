package com.vtnq.web.DTOs.Account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class RegisterUser {
    @NotBlank(message = "Full Name is required")
    private String fullName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @NotBlank(message = "Email is required")
    private String email;
    private String accountType;
    @NotBlank(message = "Password is required")
    private String password;
    @Min(value = 1,message = "City is required")
    private int cityId;
    private String address;
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Min(value = 1,message = "Country is required")
    private Integer country_id;
    @NotBlank(message = "Phone is required")
    private String phone;
    public Integer getCountry_id() {
        return country_id;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
