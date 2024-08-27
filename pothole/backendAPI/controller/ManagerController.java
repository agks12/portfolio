package com.h2o.poppy.controller;

import com.h2o.poppy.entity.Manager;
import com.h2o.poppy.model.manager.ManagerDto;
import com.h2o.poppy.model.user.UserDto;
import com.h2o.poppy.service.ManagerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public List<ManagerDto> getAllManager() {
        return managerService.getAllManager();
    }

    @GetMapping("/{managerPk}")
    public ManagerDto getIdManager(@PathVariable Long managerPk) {
        return managerService.getIdManager(managerPk);
    }

    @PostMapping("/duplicate-id")
    public Object duplicateId(@RequestBody ManagerDto data) {
        boolean result = managerService.duplicateId(data.getLoginId());
        @Getter
        class duplicateIdResponse {
            private final boolean result;

            duplicateIdResponse(boolean result) {
                this.result = result;
            }
        }
        return new duplicateIdResponse(result);
    }

    @PostMapping
    public Object saveData(@RequestBody Manager data) {
        long managerPk = managerService.saveData(data);
        boolean success = managerPk > 0;

        @Getter
        class SaveResponse {
            private final boolean success;
            private final long managerPk;

            SaveResponse(boolean success, long managerPk) {
                this.success = success;
                this.managerPk = managerPk;
            }
        }
        return new SaveResponse(success, managerPk);
    }

    @PutMapping("/{managerPk}")
    public Object updateData(@PathVariable long managerPk, @RequestBody ManagerDto data) {
        data.setManagerPk(managerPk);
        ManagerDto managerDto = managerService.getIdManager(managerPk);
        if(!Objects.equals(managerDto.getManagerName(), data.getManagerName())) {
            return "invalid value";
        }
        int state = managerService.updateData(data);
        @Getter
        class UpdateDataResponse {
            private final boolean result;

            UpdateDataResponse(int state) {
                this.result = state != 0;
            }
        }
        return new UpdateDataResponse(state);
    }

    @DeleteMapping("/{managerPk}")
    public Object deleteData(@PathVariable Long managerPk) {
        boolean result = managerService.deleteData(managerPk);

        @Getter
        class DeleteDataResponse {
            private final boolean result;

            DeleteDataResponse(boolean result){
                this.result = result;
            }
        }
        return new DeleteDataResponse(result);
    }

    @PostMapping("/login")
    public Object login(@RequestBody ManagerDto data) {
        long managerPk = managerService.login(data);
        @Getter
        class LoginResponse {
            private final boolean result;
            private final long managerPk;

            LoginResponse(long managerPk) {
                this.managerPk = managerPk;
                this.result = managerPk != 0;
            }
        }
        return new LoginResponse(managerPk);
    }
}
