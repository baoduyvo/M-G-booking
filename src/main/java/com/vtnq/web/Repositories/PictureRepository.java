package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Hotel.ImageHotelListDTO;
import com.vtnq.web.Entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Integer> {
    @Query("SELECT  a from Picture a where a.airlineId = :id")
    public Picture findByImageId(@Param("id") Integer id);
    @Query("select a from Picture a where a.roomId= :id")
    public List<Picture> findByRoomId(@Param("id") Integer id);
    @Query("SELECT a from Picture a where a.hotelId = :id")
    public List<Picture> FindImageInDetailHotel(int id);

    @Query("SELECT  a from Picture a where a.hotelId = :id and a.isMain=true")
    public Picture findByHotelId(@Param("id") Integer id);

    @Query("select new com.vtnq.web.DTOs.Hotel.ImageHotelListDTO(a.id,a.imageUrl,a.hotelId) from Picture a where a.hotelId = :id and a.isMain=false ")
    List<ImageHotelListDTO> findImageHotel(@Param("id") int id);
    @Query("SELECT a FROM Picture a where a.roomId = :id")
    List<Picture> findByRoomId(int id);
}