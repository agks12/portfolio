package com.h2o.poppy.model.blackboxvideometadata;

import com.h2o.poppy.entity.AccidentReport;
import com.h2o.poppy.entity.SerialList;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BlackboxVideoMetadataDto {
    private Long videoPk;
    private Long serialPk;
    private Date detectedAt;
    private Double latitude;
    private Double longitude;
    private String fileName;
    private AccidentReport accidentReport;

    public BlackboxVideoMetadataDto() {
    }

    public BlackboxVideoMetadataDto(Long videoPk, Long serialPk, Date detectedAt, Double latitude, Double longitude, String fileName) {
        this.videoPk = videoPk;
        this.serialPk = serialPk;
        this.detectedAt = detectedAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }
}
