package com.h2o.poppy.repository;

import com.h2o.poppy.entity.SerialList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerialListRepository extends JpaRepository<SerialList, Long> {
    SerialList findBySerialNumber(String serialNumber);
}
