package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, Integer> {
    @Query("SELECT a from RoomAmenity a where a.amenity.id = :idAmenity")
    public RoomAmenity findByAmenityId(int idAmenity);
}