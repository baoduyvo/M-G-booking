package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Account.AdminAccountList;
import com.vtnq.web.DTOs.Account.UserAccountDTO;
import com.vtnq.web.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(value = "FROM Account a WHERE a.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);
    @Query("SELECT a from Account a where a.email = :email and a.accountType= 'ROLE_ADMIN'")
    Account findByEmailRoleAdmin(String email);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.email = :email")
    boolean existsByEmail(@Param("email") String email);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.phone = :phone")
    boolean existsByPhone(@Param("phone") String phone);
    @Query("select count(a) from Account a where a.fullName = :fullname")
    boolean existsByFullname(@Param("fullname") String fullname);
    @Query("select new com.vtnq.web.DTOs.Account.AdminAccountList(a.fullName,a.id,b.name,a.phone) from Account a join Country b on b.id=a.countryId where a.accountType='ROLE_ADMIN'")
    List<AdminAccountList> getAdmin();
    @Query("select a from Account a where a.email = :email and a.otp = :otp")
    Account checkOTP(@Param("email") String email, @Param("otp") String otp);
    @Query("select new com.vtnq.web.DTOs.Account.UserAccountDTO(" +
            "a.id, COALESCE(a.avatar, null ), COALESCE(a.cityId, 0), COALESCE(a.address, ''), " +
            "COALESCE(a.email, ''), COALESCE(a.username, ''), COALESCE(a.fullName, ''), " +
            "COALESCE(a.accountType, ''), COALESCE(a.countryId, 0) ,COALESCE(a.phone, ''),a.level.id,a.password) " +
            "from Account a where a.id = :id and a.accountType = 'ROLE_USER'")
    UserAccountDTO GetUser(@Param("id") int id);
    @Query("select CASE  When count(a)>0 then true else false end from Account a where a.accountType='ROLE_ADMIN' AND a.countryId= :countryId")
    boolean existsAccountByCountryId(int countryId);
    @Query("select count(a) from Account a where a.accountType='ROLE_ADMIN'")
    int CountAdminAccount();
}