package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;
import com.vtnq.web.DTOs.AmenityDto;
import com.vtnq.web.Entities.Amenity;
import com.vtnq.web.Entities.Picture;
import com.vtnq.web.Entities.Room;
import com.vtnq.web.Entities.RoomAmenity;
import com.vtnq.web.Repositories.AmenityRepository;
import com.vtnq.web.Repositories.RoomAmenityRepository;
import com.vtnq.web.Repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenitiesServiceImplement implements AmenitiesService{
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoomAmenityRepository roomAmenityRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public boolean addAmenity(AmenityDto amenity) {
       try {
           Room room=roomRepository.findById(amenity.getRoom_id());

           Amenity amenityEntity = modelMapper.map(amenity, Amenity.class);
           amenityEntity = amenityRepository.save(amenityEntity);
           RoomAmenity roomAmenity = new RoomAmenity();
           roomAmenity.setAmenity(amenityEntity);
           roomAmenity.setRoom(room);
           roomAmenityRepository.save(roomAmenity);
           return true;
       }catch (Exception e) {
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public List<Amenity> findAll(int id) {
        try {
            return amenityRepository.findAllById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AmenityDto findById(int id) {
        try {
            return modelMapper.map(amenityRepository.findById(id), new TypeToken<AmenityDto>() {}.getType());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(AmenityDto amenity) {
        try {
            Amenity amenityEntity = modelMapper.map(amenity, Amenity.class);
            amenityRepository.save(amenityEntity);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            RoomAmenity roomAmenity=roomAmenityRepository.findByAmenityId(id);
            roomAmenityRepository.delete(roomAmenity);
            amenityRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Amenity> FindAmenitiesByHotel(int hotel_id) {
        try {
            return amenityRepository.findAmenitiesByHotelId(hotel_id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AmenitiesList> FindAmenitiesByRoom(int room_id) {
        try {
            return amenityRepository.FindAmenitiesByRoomId(room_id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Picture> FindPictureByRoom(int id) {
        try {
            return amenityRepository.FindPictureRoom(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
