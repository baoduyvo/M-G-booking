package com.vtnq.web.DTOs;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Level}
 */
public class LevelDto implements Serializable {
    private String name;

    public LevelDto() {
    }

    public LevelDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}