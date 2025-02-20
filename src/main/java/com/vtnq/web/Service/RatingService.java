package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Rating.RatingDTO;
import com.vtnq.web.Entities.Rating;

import java.util.List;

public interface RatingService {
    public boolean addRating(Rating rating);
    public double getAverageRating(int id);
    public List<RatingDTO>FindRatingByHotelId(int hotelId);
    public boolean existBookingRating(int hotelId,int userId);
}
