package com.h2o.poppy.model.manager;


import com.h2o.poppy.entity.Manager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ManagerDto {

    private Long managerPk;
    private String loginId;
    private String password;
    private String managerName;
    private String phoneNumber;


    public ManagerDto() {
    }

    public ManagerDto(Manager manager) {
        this.managerPk = manager.getManagerPk();
        this.loginId = manager.getLoginId();
        this.password = manager.getPassword();
        this.managerName = manager.getManagerName();
        this.phoneNumber = manager.getPhoneNumber();
    }
}
