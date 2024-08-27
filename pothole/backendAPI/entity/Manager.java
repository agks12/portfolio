package com.h2o.poppy.entity;
//package com.example.demo.demo.enttiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "managers")
@Getter
@Setter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_pk", nullable = false, updatable = false)
    private Long managerPk;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "manager_name", nullable = false, length = 255)
    private String managerName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    public Manager() {
    }

    public Manager(String loginId, String password, String managerName, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.managerName = managerName;
        this.phoneNumber = phoneNumber;
    }

}
