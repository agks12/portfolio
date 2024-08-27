package com.h2o.poppy.service;

import com.h2o.poppy.entity.User;
import com.h2o.poppy.model.user.UserDto;
import com.h2o.poppy.repository.AccidentReportRepository;
import com.h2o.poppy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccidentReportRepository accidentReportRepository;

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserPk(user.getUserPk());
        userDto.setLoginId(user.getLoginId());
        userDto.setPassword(user.getPassword());
        userDto.setUserName(user.getUserName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    @Autowired
    public UserService(UserRepository userRepository, AccidentReportRepository accidentReportRepository) {
        this.userRepository = userRepository;
        this.accidentReportRepository = accidentReportRepository;
    }

    public List<UserDto> getAllUser() {
        List<User> getUser = userRepository.findAll();
        return getUser.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getIdUser(Long userPk) {
        Optional<User> optionalUser = userRepository.findById(userPk);
        return optionalUser.map(this::convertToDto).orElse(null);
    }

    public boolean duplicateId(String loginId) {
        User userid = userRepository.findByLoginId(loginId);
        if (userid != null) {
            return false;
        } else {
            return true;
        }
    }

    public long saveData(User data) {
        try {
            long userPk = data.getUserPk();
            return 0;
        }catch (Exception e){
            try {
                long nowPk = 0;
                userRepository.save(data);
                nowPk = data.getUserPk();
                return nowPk;
            } catch (Exception e1) {
                return 0;
            }
        }
    }

    public int updateData(UserDto data) {
        Long userPk = data.getUserPk();
        String replacePassword = data.getPassword();
        String replacePhoneNumber = data.getPhoneNumber();
        int state = 0;
        try {
            if (replacePassword != null && replacePhoneNumber != null) {
                userRepository.updatePassWord(userPk, replacePassword);
                userRepository.updatePhoneNumber(userPk, replacePhoneNumber);
                state = 1;
            } else if (replacePassword != null) {
                userRepository.updatePassWord(userPk, replacePassword);
                state = 2;

            } else if (replacePhoneNumber != null) {
                userRepository.updatePhoneNumber(userPk, replacePhoneNumber);
                state = 3;
            }
            return state;
        } catch (Exception e) {
            System.out.println("update operation failed");
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteData(Long userPk) {
        try {
            accidentReportRepository.deleteByUserPk(userPk);
            userRepository.deleteById(userPk);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long login(UserDto data) {
        String loginId = data.getLoginId();
        String loginPassword = data.getPassword();
        User user = userRepository.findByLoginId(loginId);

        if (user != null && user.getPassword().equals(loginPassword)) {
            return user.getUserPk();
        } else {
            return 0;
        }
    }
}
