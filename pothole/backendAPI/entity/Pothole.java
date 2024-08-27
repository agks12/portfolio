package com.h2o.poppy.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;

@Entity
@Table(name = "potholes")
@Getter
@Setter
public class Pothole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pothole_pk", nullable = false, updatable = false)
    private Long potholePk;

    @Column(name = "location", nullable = false)
    private Geometry location;

    @Column(name = "is_pothole", nullable = true)
    private Boolean isPothole;

    @Column(name = "province", nullable = true, length = 10)
    private String province;

    @Column(name = "city", nullable = true, length = 10)
    private String city;

    @Column(name = "street", nullable = true, length = 50)
    private String street;

    @Column(name = "detected_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date detectedAt;

    @Column(name = "state", nullable = true, length = 50)
    private String state;

    @Column(name = "start_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "expect_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectAt;

    @Column(name = "end_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endAt;

    @Column(name = "content", nullable = true, length = 255)
    private String content;

    @OneToMany(mappedBy = "potholePk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccidentReport> accidentReports;

    public Pothole() {
    }

    public Pothole(Double latitude, Double longitude, Boolean isPothole, String province, String city, String street, Date detectedAt, String state, Date startAt, Date expectAt, Date endAt, String content) {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.location = geometryFactory.createPoint(new Coordinate(longitude, latitude)); // 경도, 위도 순서 주의
        this.isPothole = isPothole;
        this.province = province;
        this.city = city;
        this.street = street;
        this.detectedAt = detectedAt;
        this.state = state;
        this.startAt = startAt;
        this.expectAt = expectAt;
        this.endAt = endAt;
        this.content = content;
    }
}
