package com.h2o.poppy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk", nullable = false, updatable = false)
    private Long userPk;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @OneToOne(mappedBy = "userPk", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UsersSerials usersSerials;

    @OneToMany(mappedBy = "userPk", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccidentReport> accidentReport;

    public User() {
    }

    public User(String loginId, String password, String userName, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

}

