package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Rating.RatingDTO;
import com.vtnq.web.Entities.Rating;
import com.vtnq.web.Repositories.HotelRepository;
import com.vtnq.web.Repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImplement implements RatingService {
 @Autowired
 private RatingRepository ratingRepository;
 @Autowired
 private HotelRepository hotelRepository;
    @Override
    public boolean addRating(Rating rating) {
        try {

            rating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(rating);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public double getAverageRating(int id) {
        try {
            return ratingRepository.AvgRatingByHotel(id);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<RatingDTO> FindRatingByHotelId(int hotelId) {
        try {
            return ratingRepository.findRatingByHotelId(hotelId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existBookingRating(int hotelId, int userId) {
        try {
            return hotelRepository.existBooking(hotelId, userId);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
