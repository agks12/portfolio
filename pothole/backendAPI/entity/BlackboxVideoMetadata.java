package com.h2o.poppy.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "blackbox_video_metadatas")
@Getter
@Setter
public class BlackboxVideoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_pk", nullable = false, updatable = false)
    private Long videoPk;

    @ManyToOne
    @JoinColumn(name = "serial_pk", nullable = false)
    private SerialList serialPk;

    @Column(name = "detected_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date detectedAt;

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @OneToOne(mappedBy = "videoPk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AccidentReport accidentReport;

    public BlackboxVideoMetadata() {
    }

    public BlackboxVideoMetadata(SerialList serialPk, Date detectedAt, Double latitude, Double longitude, String fileName) {
        this.serialPk = serialPk;
        this.detectedAt = detectedAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
    }

}
