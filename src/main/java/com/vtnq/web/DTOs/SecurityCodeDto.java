package com.vtnq.web.DTOs;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.vtnq.web.Entities.SecurityCode}
 */
public class SecurityCodeDto implements Serializable {
    private String valueCode;
    private LocalDate startAt;
    private LocalDate endAt;
    private String location;
    private LocalDate dob;
    private String frontSecurityCode;
    private String backSecurityCode;

    public SecurityCodeDto() {
    }

    public SecurityCodeDto(String valueCode, LocalDate startAt, LocalDate endAt, String location, LocalDate dob, String frontSecurityCode, String backSecurityCode) {
        this.valueCode = valueCode;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.dob = dob;
        this.frontSecurityCode = frontSecurityCode;
        this.backSecurityCode = backSecurityCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getFrontSecurityCode() {
        return frontSecurityCode;
    }

    public void setFrontSecurityCode(String frontSecurityCode) {
        this.frontSecurityCode = frontSecurityCode;
    }

    public String getBackSecurityCode() {
        return backSecurityCode;
    }

    public void setBackSecurityCode(String backSecurityCode) {
        this.backSecurityCode = backSecurityCode;
    }
}