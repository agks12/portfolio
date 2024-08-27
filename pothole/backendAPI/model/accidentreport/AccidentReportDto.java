package com.h2o.poppy.model.accidentreport;


import com.h2o.poppy.entity.BlackboxVideoMetadata;
import com.h2o.poppy.entity.Pothole;
import com.h2o.poppy.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AccidentReportDto {

    private Long reportPk;
    private Long userPk;
    private Long potholePk;
    private Long videoPk;
    private String reportName;
    private String reportContent;
    private Date reportDate;
    private String state;
    private String rejectionReason;

    public AccidentReportDto(Long reportPk, Long userPk, Long potholePk, Long videoPk, String reportContent, String reportName, Date reportDate,String state,String rejectionReason) {
        this.reportPk = reportPk;
        this.userPk = userPk;
        this.potholePk = potholePk;
        this.videoPk = videoPk;
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportDate = reportDate;
        this.state = state;
        this.rejectionReason = rejectionReason;
    }

}
