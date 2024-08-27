package com.h2o.poppy.model.blackboxvideometadata;

import com.h2o.poppy.entity.AccidentReport;
import com.h2o.poppy.entity.SerialList;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BlackboxVideoMetadataJoinUserDto {
    private Long videoPk;
    private Long serialPk;
    private String serialNumber;
    private Date detectedAt;
    private Double latitude;
    private Double longitude;
    private String fileName;
    private AccidentReport accidentReport;

    public BlackboxVideoMetadataJoinUserDto() {
    }

    public BlackboxVideoMetadataJoinUserDto(Long videoPk, Long serialPk, String serialNumber, Date detectedAt, Double latitude, Double longitude, String fileName) {
        this.videoPk = videoPk;
        this.serialPk = serialPk;
        this.serialNumber = serialNumber;
        this.detectedAt = detectedAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }
}
