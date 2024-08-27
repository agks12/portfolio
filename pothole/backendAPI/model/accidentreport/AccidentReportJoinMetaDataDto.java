package com.h2o.poppy.model.accidentreport;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccidentReportJoinMetaDataDto {

    private Long reportPk;
    private String userName;
    private Long potholePk;
    private Long videoPk;
    private String serialNumber;
    private Double latitude;
    private Double longitude;
    private String reportName;
    private String reportContent;
    private Date reportDate;
    private String state;
    private String rejectionReason;

    public AccidentReportJoinMetaDataDto(Long reportPk, String userName, Long potholePk, Long videoPk, String serialNumber,Double latitude,Double longitude, String reportContent, String reportName, Date reportDate,String state,String rejectionReason) {
        this.reportPk = reportPk;
        this.userName = userName;
        this.potholePk = potholePk;
        this.videoPk = videoPk;
        this.serialNumber = serialNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportDate = reportDate;
        this.state = state;
        this.rejectionReason = rejectionReason;
    }

}
