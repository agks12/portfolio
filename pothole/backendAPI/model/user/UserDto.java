package com.h2o.poppy.model.user;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDto {

    private Long userPk;
    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;


    public UserDto() {
    }

}
