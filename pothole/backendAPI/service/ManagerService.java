package com.h2o.poppy.service;

import com.h2o.poppy.entity.Manager;
import com.h2o.poppy.model.manager.ManagerDto;
import com.h2o.poppy.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    private ManagerDto convertToDto(Manager manager) {
        ManagerDto managerDto = new ManagerDto();
        managerDto.setManagerPk(manager.getManagerPk());
        managerDto.setLoginId(manager.getLoginId());
        managerDto.setPassword(manager.getPassword());
        managerDto.setManagerName(manager.getManagerName());
        managerDto.setPhoneNumber(manager.getPhoneNumber());
        return managerDto;
    }

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<ManagerDto> getAllManager() {
        List<Manager> getManager = managerRepository.findAll();
        return getManager.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ManagerDto getIdManager(Long managerPk) {
        Optional<Manager> optionalManager = managerRepository.findById(managerPk);
        return optionalManager.map(this::convertToDto).orElse(null);
    }

    public boolean duplicateId(String loginId) {
        Manager managerid = managerRepository.findByLoginId(loginId);
        if (managerid != null) {
            return false;
        } else {
            return true;
        }
    }

    public long saveData(Manager data) {
        try{
            long managerPk = data.getManagerPk();
            return 0;
        }catch (Exception e){
            try {
                long nowPk = 0;
                managerRepository.save(data);
                nowPk = data.getManagerPk();
                return nowPk;
            } catch (Exception e1) {
                return 0;
            }
        }
    }

    public int updateData(ManagerDto data) {
        Long managerpk = data.getManagerPk();
        String replacePassword = data.getPassword();
        String replacePhoneNumber = data.getPhoneNumber();
        int state = 0;
        try {
            if (replacePassword != null && replacePhoneNumber != null) {
                managerRepository.updatePassWord(managerpk, replacePassword);
                managerRepository.updatePhoneNumber(managerpk, replacePhoneNumber);
                state = 1;
            } else if (replacePassword != null) {
                managerRepository.updatePassWord(managerpk, replacePassword);
                state = 2;

            } else if (replacePhoneNumber != null) {
                managerRepository.updatePhoneNumber(managerpk, replacePhoneNumber);
                state = 3;
            }
            return state;
        } catch (Exception e) {
            System.out.println("update operation failed");
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteData(Long managerPk) {
        try {
            managerRepository.deleteById(managerPk);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long login(ManagerDto data) {
        String loginId = data.getLoginId();
        String loginPassword = data.getPassword();
        Manager managerid = managerRepository.findByLoginId(loginId);
        Manager managerPassword = managerRepository.findByPassword(loginPassword);

        if (managerid != null && managerPassword != null) {
            Manager info = managerRepository.findManagerPkByLoginId(loginId);
            long managerPk = info.getManagerPk();
            return managerPk;
        } else {
            return 0;
        }
    }
}
