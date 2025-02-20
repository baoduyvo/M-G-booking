package com.vtnq.web.DTOs;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.vtnq.web.Entities.Rating}
 */
public class RatingDto implements Serializable {
    private String content;
    private Double rating;
    private LocalDate createdAt;

    public RatingDto() {
    }

    public RatingDto(String content, Double rating, LocalDate createdAt) {
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}