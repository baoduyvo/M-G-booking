package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("select a from District  a where a.city.id = :id order by a.id desc")
    List<District> findDistrictByCityId(int id);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM District a WHERE a.name = :name")
    Boolean existsByName(String name);
}