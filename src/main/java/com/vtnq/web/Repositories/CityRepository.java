package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("select a from City a where a.country.id = :id order by a.id desc")
    public List<City> findByCountryId(int id);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM City a WHERE a.name = :name")
    boolean existsByName(String name);

    @Query("select a from City a where a.country.name like %:name% or a.name like %:name% order by a.id desc limit 5")
    List<City> SearchCityOrCountry(String name);
    @Query("select count(a) from City a where a.country.id = :id")
    public int CountCity(int id);

}