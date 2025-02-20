package com.vtnq.web.DTOs.Account;

public class AdminAccountList {
    private String fullName;
    private int id;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AdminAccountList(String fullName, int id, String nameCountry,String phone) {
        this.fullName = fullName;
        this.id = id;
        this.nameCountry = nameCountry;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    private String nameCountry;
}
