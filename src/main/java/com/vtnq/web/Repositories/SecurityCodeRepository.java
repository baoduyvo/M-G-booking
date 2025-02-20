package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Account.UserAccountDTO;
import com.vtnq.web.Entities.SecurityCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SecurityCodeRepository extends JpaRepository<SecurityCode, Integer> {

}