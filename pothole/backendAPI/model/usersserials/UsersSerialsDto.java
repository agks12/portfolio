package com.h2o.poppy.model.usersserials;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersSerialsDto {
    private Long userSerialPk;
    private Long userPk;
    private Long serialPk;

    public UsersSerialsDto(){
    }
}
