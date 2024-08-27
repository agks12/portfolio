package com.h2o.poppy.repository;

import com.h2o.poppy.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    User findByUserPk(Long userPk);
    User findByLoginId(String loginId);
    User findByPassword(String password);
    User findUserPkByLoginId(String loginId);

    @Transactional
    @Modifying
    @Query("UPDATE User e SET e.password = :newData WHERE e.userPk = :userPk")
    int updatePassWord(@Param("userPk") long Pk, @Param("newData") String newValue);
    @Transactional
    @Modifying
    @Query("UPDATE User e SET e.phoneNumber = :newData WHERE e.userPk = :userPk")
    int updatePhoneNumber(@Param("userPk") long Pk, @Param("newData") String newValue);
}
