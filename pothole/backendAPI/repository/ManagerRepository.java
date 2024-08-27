package com.h2o.poppy.repository;

import com.h2o.poppy.entity.Manager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    List<Manager> findAll();
    Manager findByLoginId(String loginId);
    Manager findByPassword(String password);
    Manager findManagerPkByLoginId(String loginId);

    @Transactional
    @Modifying
    @Query("UPDATE Manager e SET e.password = :newData WHERE e.managerPk = :managerPk")
    int updatePassWord(@Param("managerPk") long Pk, @Param("newData") String newValue);
    @Transactional
    @Modifying
    @Query("UPDATE Manager e SET e.phoneNumber = :newData WHERE e.managerPk = :managerPk")
    int updatePhoneNumber(@Param("managerPk") long Pk, @Param("newData") String newValue);
}
