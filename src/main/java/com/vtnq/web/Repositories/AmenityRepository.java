package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;
import com.vtnq.web.Entities.Amenity;
import com.vtnq.web.Entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {
    @Query("select a from Amenity  a join RoomAmenity b on a.id=b.amenity.id where b.room.id= :roomId")
    List<Amenity>findAllById(int roomId);
    @Query("select distinct a from Amenity a " +
            "join RoomAmenity b on a.id=b.amenity.id " +
            "join Room c on c.id=b.room.id " +
            "join Hotel d on d.id=c.hotel.id " +
            "where c.hotel.id = :hotelId")
    List<Amenity>findAmenitiesByHotelId(int hotelId);
    @Query("SELECT new com.vtnq.web.DTOs.Amenities.AmenitiesList(a.id,a.name) from Amenity a join RoomAmenity b on a.id=b.amenity.id where b.room.id= :roomId")
    List<AmenitiesList>FindAmenitiesByRoomId(int roomId);
    @Query("SELECT a from Picture  a join Room b on a.roomId=b.id where b.id= :id")
    List<Picture>FindPictureRoom(int id);

}