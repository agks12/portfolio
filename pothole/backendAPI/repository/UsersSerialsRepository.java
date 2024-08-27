package com.h2o.poppy.repository;

import com.h2o.poppy.entity.User;
import com.h2o.poppy.entity.UsersSerials;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersSerialsRepository extends JpaRepository<UsersSerials, Long> {
    List<UsersSerials> findAll();

    @Transactional
    @Query("SELECT u FROM UsersSerials u WHERE u.userPk.userPk = :userPk")
    UsersSerials findByUser(@Param("userPk") Long userPk);
}
