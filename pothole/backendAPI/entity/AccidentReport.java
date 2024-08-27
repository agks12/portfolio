package com.h2o.poppy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "accident_report")
@Getter
@Setter
public class AccidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_pk", nullable = false, updatable = false)
    private Long reportPk;

    @ManyToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User userPk;

    @ManyToOne
    @JoinColumn(name = "pothole_pk", nullable = false)
    private Pothole potholePk;

    @OneToOne
    @JoinColumn(name = "video_pk", nullable = false)
    private BlackboxVideoMetadata videoPk;

    @Column(name = "report_name", length = 255)
    private String reportName;

    @Column(name = "report_content", length = 255)
    private String reportContent;

    @Column(name = "report_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    public AccidentReport() {
    }

    public AccidentReport(User userPk, Pothole potholePk, BlackboxVideoMetadata videoPk, String reportContent,String reportName, Date reportDate, String state,String rejectionReason) {
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
