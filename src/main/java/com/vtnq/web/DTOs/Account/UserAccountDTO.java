package com.vtnq.web.DTOs.Account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;

public class UserAccountDTO {
    private Integer id;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    private String phone;
    private String fullName;
    private String username;
    private String email;
    private int level_id;

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public UserAccountDTO() {
    }

    @JsonCreator
    public UserAccountDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("avatar") String avatar,
            @JsonProperty("cityId") int cityId,
            @JsonProperty("address") String address,
            @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("accountType") String accountType,
            @JsonProperty("countryId") int countryId,
            @JsonProperty("phone") String phone,
            @JsonProperty("level_id") int level_id,

            @JsonProperty("password")String password) {
        this.id = id;
        this.Avatar = avatar;
        this.CityId = cityId;
        this.Address = address;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.AccountType = accountType;
        this.CountryId = countryId;
        this.phone = phone;

        this.level_id = level_id;
        this.password = password;
    }




    private String Address;
    private int CityId;

    private String Avatar;
    private MultipartFile AvatarFile;
    private String AccountType;
    private int CountryId;
    private String ValueCode;

    public LocalDate getStartAt() {
        return StartAt;
    }

    public void setStartAt(LocalDate startAt) {
        StartAt = startAt;
    }




    private LocalDate StartAt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }


    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public MultipartFile getAvatarFile() {
        return AvatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        AvatarFile = avatarFile;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }





}
