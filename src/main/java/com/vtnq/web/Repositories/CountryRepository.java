package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query("SELECT CASE WHEN count(a)>0 THEN TRUE ELSE FALSE END FROM Country a WHERE a.name = :name")
    boolean existsByCountry(String name);
    @Query("select count(a)from Country a")
    int CountCountry();
}