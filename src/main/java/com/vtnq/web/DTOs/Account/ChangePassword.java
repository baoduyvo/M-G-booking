package com.vtnq.web.DTOs.Account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePassword {
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("Email")
    private String Email;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
