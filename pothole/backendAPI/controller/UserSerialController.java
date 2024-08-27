package com.h2o.poppy.controller;

import com.h2o.poppy.entity.SerialList;
import com.h2o.poppy.entity.UsersSerials;
import com.h2o.poppy.model.usersserials.UsersSerialsDto;
import com.h2o.poppy.service.UsersSerialsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users-serials")
public class UserSerialController {
    private final UsersSerialsService usersSerialsService;

    @Autowired
    public UserSerialController(UsersSerialsService usersSerialsService) {
        this.usersSerialsService = usersSerialsService;
    }

    @GetMapping
    public List<UsersSerialsDto> getAllUsersSerials() {
        return usersSerialsService.getAllUsersSerials();
    }

    @GetMapping("/{userPk}")
    public Object getSerial(@PathVariable Long userPk){
        SerialList serial = usersSerialsService.getUserSerial(userPk);
        @Getter
        class getSerialResponse{
            private final String serialNumber;
            getSerialResponse(SerialList serial){
                this.serialNumber = serial.getSerialNumber();
            }
        }
        return new getSerialResponse(serial);
    }
}
