package com.h2o.poppy.repository;

import com.h2o.poppy.entity.AccidentReport;
import com.h2o.poppy.entity.BlackboxVideoMetadata;
import com.h2o.poppy.model.blackboxvideometadata.BlackboxVideoMetadataDto;
import com.h2o.poppy.model.blackboxvideometadata.BlackboxVideoMetadataJoinUserDto;
import com.h2o.poppy.model.pothole.PotholeDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlackboxVideoMetadataRepository extends JpaRepository<BlackboxVideoMetadata, Long> {
    @Query("SELECT new com.h2o.poppy.model.blackboxvideometadata.BlackboxVideoMetadataDto(bvm.videoPk, bvm.serialPk.serialPk, bvm.detectedAt, bvm.latitude, bvm.longitude, bvm.fileName) FROM BlackboxVideoMetadata bvm WHERE bvm.videoPk = :videoPk")
    BlackboxVideoMetadataDto getBlackboxVideoMetadataByVideoId(@Param("videoPk") Long Pk);

    @Transactional
    @Modifying
    @Query("UPDATE BlackboxVideoMetadata e SET e.latitude = :newData WHERE e.videoPk = :videoPk")
    int updatelatitude(@Param("videoPk") long Pk, @Param("newData") double newValue);

    @Transactional
    @Modifying
    @Query("UPDATE BlackboxVideoMetadata e SET e.longitude = :newData WHERE e.videoPk = :videoPk")
    int updatelongitude(@Param("videoPk") long Pk, @Param("newData") double newValue);

    @Query("SELECT new com.h2o.poppy.model.blackboxvideometadata.BlackboxVideoMetadataJoinUserDto(bvm.videoPk, bvm.serialPk.serialPk, bvm.serialPk.serialNumber, bvm.detectedAt, bvm.latitude, bvm.longitude, bvm.fileName) FROM BlackboxVideoMetadata bvm WHERE bvm.serialPk = (SELECT us.serialPk FROM UsersSerials us WHERE us.userPk.userPk = :userPk)")
    List<BlackboxVideoMetadataJoinUserDto> findByJoinUserPk(@Param("userPk") Long userPk);
}
