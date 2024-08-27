package com.h2o.poppy.controller;

import com.h2o.poppy.entity.*;
import com.h2o.poppy.model.accidentreport.AccidentReportJoinMetaDataDto;
import com.h2o.poppy.model.blackboxvideometadata.BlackboxVideoMetadataDto;
import com.h2o.poppy.model.pothole.PotholeDto;
import com.h2o.poppy.repository.AccidentReportRepository;
import com.h2o.poppy.model.accidentreport.AccidentReportDto;
import com.h2o.poppy.service.AccidentReportService;
import com.h2o.poppy.service.BlackboxVideoMetadataService;

import com.h2o.poppy.repository.BlackboxVideoMetadataRepository;
import com.h2o.poppy.repository.UserRepository;
import com.h2o.poppy.repository.PotholeRepository;
import com.h2o.poppy.service.AccidentReportService;

import com.h2o.poppy.service.S3Service;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/accident-report")
public class AccidentReportController {

    private final AccidentReportRepository accidentReportRepository;
    private final AccidentReportService accidentReportService;
    private final BlackboxVideoMetadataService blackboxVideoMetadataService;
    private final S3Service s3Service;


    public AccidentReportController(AccidentReportRepository accidentReportRepository,
            AccidentReportService accidentReportService, S3Service s3Service, BlackboxVideoMetadataService blackboxVideoMetadataService) {
        this.accidentReportRepository = accidentReportRepository;
        this.accidentReportService = accidentReportService;
        this.blackboxVideoMetadataService = blackboxVideoMetadataService;
        this.s3Service = s3Service;
    }

    @PostMapping
    public Object saveData(@RequestParam("userPk") Long userPk,@RequestParam("videoPk") Long videoPk,@RequestParam("reportName") String reportName,@RequestParam("reportContent") String reportContent,@RequestParam("file") List<MultipartFile> image)throws IOException {
        AccidentReportJoinMetaDataDto result = accidentReportService.saveData(userPk,videoPk,reportName,reportContent);
        boolean success = result != null;

        if(success){
            List<MultipartFile> fileList = image;
            String reportPk = Long.toString(result.getReportPk());
            String serialNumber = result.getSerialNumber();
            s3Service.createFolder(serialNumber+"/"+reportPk);
            for(MultipartFile nowFile : fileList ){
                s3Service.uploadFile(serialNumber+"/"+reportPk, nowFile);
            }
        }

        @Getter
        class SaveResponse {
            private final boolean success;
            private final AccidentReportJoinMetaDataDto result;

            SaveResponse(boolean success, AccidentReportJoinMetaDataDto result) {
                this.success = success;
                this.result = result;
            }
        }

        return new SaveResponse(success, result);
    }


    @GetMapping("/user/{userPk}")
    public Object getIduser(@PathVariable Long userPk) {

        List<AccidentReportJoinMetaDataDto> result = accidentReportService.getAccident(userPk);
        boolean success;

        if (result != null)
            success = true;
        else
            success = false;

        @Getter
        class SaveResponse {
            private final boolean success;
            private final List<AccidentReportJoinMetaDataDto> result;

            SaveResponse(boolean success, List<AccidentReportJoinMetaDataDto> result) {
                this.success = success;
                this.result = result;
            }
        }

        return new SaveResponse(success, result);
    }

    @GetMapping("/{reportPk}")
    public Object getIdReport(@PathVariable Long reportPk) {

        AccidentReportJoinMetaDataDto result = accidentReportService.getAccidentReportPk(reportPk);
        boolean success = result != null;

        List<String> imageFileNameList = null;

        String videoFileName = null;

        if(success){
            Long videoPk = result.getVideoPk();
            BlackboxVideoMetadataDto videoDate = blackboxVideoMetadataService.getIdBlackboxVideoMetadata(videoPk);
            videoFileName = videoDate.getFileName();
            String folderPath = result.getSerialNumber()+"/"+Long.toString(result.getReportPk());
            imageFileNameList = s3Service.listObjectsInFolder(folderPath);
            if(!imageFileNameList.isEmpty()) imageFileNameList.remove(0);
        }

        @Getter
        class SaveResponse {
            private final boolean success;
            private final AccidentReportJoinMetaDataDto result;
            private final List<String> imageFileNameList;
            private final String videoFileName;
            SaveResponse(boolean success, AccidentReportJoinMetaDataDto result, List<String> imageFileNameList, String videoFileName) {
                this.success = success;
                this.result = result;
                this.imageFileNameList = imageFileNameList;
                this.videoFileName = videoFileName;
            }
        }

        return new SaveResponse(success, result, imageFileNameList,videoFileName);
    }


    @GetMapping("/no-check")
    public Object getNoCheck() {
        List<AccidentReportJoinMetaDataDto> noCheckState = accidentReportService.getState("미확인");
        boolean success = noCheckState != null; 
        @Getter
        class getResponse {
            private final boolean success;
            private final List<AccidentReportJoinMetaDataDto> noCheckState;

            getResponse(boolean success, List<AccidentReportJoinMetaDataDto> noCheckState) {
                this.success = success;
                this.noCheckState = noCheckState;
            }
        }
        return new getResponse(success, noCheckState);
    }

    @GetMapping("/yes-check")
    public Object getYesCheck() {
        List<AccidentReportJoinMetaDataDto> yesCheckState = accidentReportService.getState("y");
        boolean success = yesCheckState != null; 
        @Getter
        class getResponse {
            private final boolean success;
            private final List<AccidentReportJoinMetaDataDto> yesCheckState;

            getResponse(boolean success, List<AccidentReportJoinMetaDataDto> yesCheckState) {
                this.success = success;
                this.yesCheckState = yesCheckState;
            }
        }
        return new getResponse(success, yesCheckState);
    }

    @Getter
    @Setter
    static class DateRequest {
        private Date reportDate;
        private String state;
    }

    @PostMapping("/date")
    public Object dateGet(@RequestBody DateRequest request) {
        Date targetDate = request.getReportDate();
        String state = request.getState();
        List<AccidentReportJoinMetaDataDto> dateList = accidentReportService.getDate(targetDate, state);
        boolean success = dateList != null; 
        @Getter
        class getResponse {
            private final boolean success;
            private final List<AccidentReportJoinMetaDataDto> dateList;

            getResponse(boolean success, List<AccidentReportJoinMetaDataDto> dateList) {
                this.success = success;
                this.dateList = dateList;
            }
        }
        return new getResponse(success, dateList);
    }


    @PatchMapping()
    public Object changeState(@RequestBody AccidentReportDto data) {
        String changeState = accidentReportService.changeState(data);
        boolean success = changeState != null;
        @Getter
        class stateResponse {
            private final boolean success;
            private final String changeState;

            stateResponse(boolean success, String changeState) {
                this.changeState = changeState;
                this.success = success;
            }
        }
        return new stateResponse(success, changeState);
    }


    @GetMapping("/pothole-list/{videoPk}")
    public Object getPotholeList(@PathVariable Long videoPk){
        List<PotholeDto> result = accidentReportService.getPotholeList(videoPk);
        boolean success = result != null;
        @Getter
        class SaveResponse {
            private final boolean success;
            private final List<PotholeDto> result;

            SaveResponse(boolean success, List<PotholeDto> result) {
                this.success = success;
                this.result = result;
            }
        }

        return new SaveResponse(success, result);
    }

}
