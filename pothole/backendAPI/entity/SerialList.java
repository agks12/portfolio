package com.h2o.poppy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "serial_lists")
@Getter
@Setter
public class SerialList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_pk", nullable = false, updatable = false)
    private Long serialPk;

    @Column(name = "serial_number", length = 20)
    private String serialNumber;

    @OneToOne(mappedBy = "serialPk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UsersSerials usersSerials;

    @OneToMany(mappedBy = "serialPk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BlackboxVideoMetadata> blackboxVideoMetadata;

    public SerialList() {
    }

    public SerialList(String serialNumber) {
        this.serialNumber = serialNumber;
    }


}
