package com.h2o.poppy.model.pothole;

import com.h2o.poppy.entity.AccidentReport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PotholeDto {

    private Long potholePk;
    private Double latitude;
    private Double longitude;
    private Boolean isPothole;
    private String province;
    private String city;
    private String street;
    private Date detectedAt;
    private String state;
    private Date startAt;
    private Date expectAt;
    private Date endAt;
    private String content;
    public PotholeDto() {
        
    }

    public PotholeDto(Long potholePk, Double latitude, Double longitude, Boolean isPothole,String province, String city,String street,Date detectedAt,String state,Date startAt,Date expectAt,Date endAt,String content) {
        this.potholePk = potholePk;
        this.latitude = latitude;
        this.longitude = longitude;
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
