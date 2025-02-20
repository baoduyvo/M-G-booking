package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Room.RoomDetailHotel;
import com.vtnq.web.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT a from Room a where a.hotel.id = :id")
    List<Room>findAll(int id);
    @Query("select a from Room  a where a.type.id=:id")
    List<Room>findAllByType(int id);
    @Query("select a from Room a where a.id = :id")
    Room findById(int id);
    @Query("select distinct new com.vtnq.web.DTOs.Room.RoomDetailHotel" +
            "(a.type.id, a.type.name, a.type.price, a.occupancy, MAX(b.imageUrl)) " +
            "from Room a " +
            "join Picture b on b.roomId = a.id " +
            "where a.hotel.id = :id " +
            "group by a.type.id, a.type.name, a.type.price, a.occupancy")
    List<RoomDetailHotel> findRoomDetailHotel(@Param("id") int id);
    @Query("select distinct new com.vtnq.web.DTOs.Room.RoomDetailHotel" +
            "( a.type.name,a.type.id, a.type.price, a.occupancy) " +
            "from Room a " +
            "where a.hotel.id = :id " +
            "group by a.type.id, a.type.name, a.type.price, a.occupancy " +
            "having COUNT(a.type.id) >= :quantityRoom")
    List<RoomDetailHotel> findRoomDetailHotelWeb(@Param("id") int id,@Param("quantityRoom") int quantityRoom);
    @Query("select a from Room  a where a.type.id = :id and a.status=false")
    List<Room> findByTypeId(int id);
    @Query("select a from Room a where a.type.id = :id and a.status=false ")
    List<Room>FindRoomType(int id);
}