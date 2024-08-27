package com.h2o.poppy.model.address;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Address {
    @Id
    private String id;
    private String address;

    public Address(String address){
        this.address = address;
        this.id = address;
    }
}
