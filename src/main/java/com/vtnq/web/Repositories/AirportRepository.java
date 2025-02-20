package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Airport.SearchAiportDTO;
import com.vtnq.web.Entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
    @Query("SELECT a from Airport a where a.city.country.id = :id order by a.id desc ")
    List<Airport> findByCountry(@Param("id") int id);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Airport a WHERE a.name = :name")
    boolean existsByName(@Param("name") String name);
    @Query("SELECT CASE WHEN COUNT(a)>0 THEN TRUE ELSE FALSE END FROM Airport a where a.city.id= :id")
    boolean existsAirportInCity(@Param("id")int id);
    @Query("SELECT new com.vtnq.web.DTOs.Airport.SearchAiportDTO(t.id,t.name,t.city,t.city.country.name) from  Airport t where t.name like %:SearchName% or  t.city.name like %:SearchName%")
    List<SearchAiportDTO>SearchAirPort(@Param("SearchName") String SearchName);
    @Query("select count(a) from Airport a where a.city.country.id= :id")
    public int CountAirPort(int id);
}