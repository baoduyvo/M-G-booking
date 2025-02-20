package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    @Query("select a from Type a where a.hotelId = :id")
    List<Type>FindByHotel(int id);
}
