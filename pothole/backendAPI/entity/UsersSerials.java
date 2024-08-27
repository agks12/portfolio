package com.h2o.poppy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_serials")
@Getter
@Setter
public class UsersSerials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_serial_pk", nullable = false, updatable = false)
    private Long userSerialPk;

    @OneToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User userPk;

    @OneToOne
    @JoinColumn(name = "serial_pk", nullable = false)
    private SerialList serialPk;

    public UsersSerials() {
    }

    public UsersSerials(User userPk, SerialList serialPk) {
        this.userPk = userPk;
        this.serialPk = serialPk;
    }
}
