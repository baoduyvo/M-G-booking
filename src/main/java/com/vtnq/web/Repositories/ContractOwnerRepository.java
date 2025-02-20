package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.ContractOwner.ContractOwnerDto;
import com.vtnq.web.Entities.ContractOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractOwnerRepository extends JpaRepository<ContractOwner, Integer> {
    @Query("SELECT new com.vtnq.web.DTOs.ContractOwner.ContractOwnerDto(" +
            "a.id,b.id,b.fullName,b.email,b.phone,b.address,c.id,c.name,a.status) from ContractOwner a join Account b on a.ownerId=b.id " +
            "join Hotel c on a.hotelId=c.id join City d on d.id=c.cityId where d.country.id = :id")
    public List<ContractOwnerDto>FindAll(int id);
}