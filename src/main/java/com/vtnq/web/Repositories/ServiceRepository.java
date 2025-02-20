package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    @Query("select a from Service a where a.hotel.id = :id")
    List<Service> findByHotel(@Param("id") int id);
}