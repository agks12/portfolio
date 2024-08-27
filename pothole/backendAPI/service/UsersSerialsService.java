package com.h2o.poppy.service;

import com.h2o.poppy.entity.SerialList;
import com.h2o.poppy.entity.UsersSerials;
import com.h2o.poppy.model.usersserials.UsersSerialsDto;
import com.h2o.poppy.repository.SerialListRepository;
import com.h2o.poppy.repository.UserRepository;
import com.h2o.poppy.repository.UsersSerialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersSerialsService {

    private final UsersSerialsRepository usersSerialsRepository;
    private final UserRepository userRepository;
    private final SerialListRepository serialListRepository;

    private UsersSerialsDto convertToDto(UsersSerials usersSerials) {
        UsersSerialsDto usersSerialsDto = new UsersSerialsDto();
        usersSerialsDto.setUserSerialPk(usersSerials.getUserSerialPk());
        usersSerialsDto.setUserPk(usersSerials.getUserPk().getUserPk());
        usersSerialsDto.setSerialPk(usersSerials.getSerialPk().getSerialPk());
        return usersSerialsDto;
    }

    @Autowired
    public UsersSerialsService(UsersSerialsRepository usersSerialsRepository, UserRepository userRepository, SerialListRepository serialListRepository) {
        this.usersSerialsRepository = usersSerialsRepository;
        this.userRepository = userRepository;
        this.serialListRepository = serialListRepository;
    }

    public List<UsersSerialsDto> getAllUsersSerials() {
        List<UsersSerials> getUsersSerials = usersSerialsRepository.findAll();
        return getUsersSerials.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SerialList getUserSerial(Long userPk){
        UsersSerials usersSerials = usersSerialsRepository.findByUser(userPk);
        return usersSerials.getSerialPk();
    }

}
