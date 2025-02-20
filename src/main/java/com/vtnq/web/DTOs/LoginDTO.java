package com.vtnq.web.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
    @JsonProperty("Email")
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    @JsonProperty("Password")
    private String Password;

}
