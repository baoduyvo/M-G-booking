package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;
import com.vtnq.web.DTOs.AmenityDto;
import com.vtnq.web.Entities.Amenity;
import com.vtnq.web.Entities.Picture;

import java.util.List;


public interface AmenitiesService {
    public boolean addAmenity(AmenityDto amenity);
    public List<Amenity>findAll(int id);
    public AmenityDto findById(int id);
    public boolean update(AmenityDto amenity);
    public boolean delete(int id);
    public List<Amenity>FindAmenitiesByHotel(int hotel_id);
    public List<AmenitiesList>FindAmenitiesByRoom(int room_id);
    public List<Picture>FindPictureByRoom(int id);
}
