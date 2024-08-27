package com.h2o.poppy.service;

import com.h2o.poppy.entity.BlackboxVideoMetadata;
import com.h2o.poppy.entity.Pothole;
import com.h2o.poppy.entity.User;
import com.h2o.poppy.entity.AccidentReport;
import com.h2o.poppy.model.accidentreport.AccidentReportJoinMetaDataDto;
import com.h2o.poppy.model.pothole.PotholeDto;
import com.h2o.poppy.repository.UserRepository;
import com.h2o.poppy.repository.AccidentReportRepository;
import com.h2o.poppy.repository.PotholeRepository;
import com.h2o.poppy.repository.BlackboxVideoMetadataRepository;
import com.h2o.poppy.model.accidentreport.AccidentReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccidentReportService {

    private final AccidentReportRepository accidentReportRepository;
    private final UserRepository userRepository;
    private final PotholeRepository potholeRepository;
    private final BlackboxVideoMetadataRepository blackboxVideoMetadataRepository;

    @Autowired
    public AccidentReportService(AccidentReportRepository accidentReportRepository,UserRepository userRepository,PotholeRepository potholeRepository,BlackboxVideoMetadataRepository blackboxVideoMetadataRepository) {
        this.accidentReportRepository = accidentReportRepository;
        this.userRepository = userRepository;
        this.potholeRepository = potholeRepository;
        this.blackboxVideoMetadataRepository = blackboxVideoMetadataRepository;
    }


    public AccidentReportJoinMetaDataDto saveData(Long userPk, Long videoPk, String reportName, String reportContent) {
        try {
            Date date = new Date();
            String state = "미확인";
            String rejectionReason = null;
            User user = userRepository.findById(userPk).orElse(null);
            Long potholePk = potholeRepository.findPothlesbyVideoPk(videoPk).get(0).getPotholePk();
            Pothole pothole = potholeRepository.findById(potholePk).orElse(null);
            BlackboxVideoMetadata blackboxVideoMetadata = blackboxVideoMetadataRepository.findById(videoPk).orElse(null);

            if (user != null && pothole != null && blackboxVideoMetadata != null) {
                AccidentReport accidentReport = new AccidentReport(user, pothole, blackboxVideoMetadata, reportContent, reportName, date,state,rejectionReason);
                accidentReportRepository.save(accidentReport);
                return new AccidentReportJoinMetaDataDto(accidentReport.getReportPk(), user.getUserName(),pothole.getPotholePk(), blackboxVideoMetadata.getVideoPk(), blackboxVideoMetadata.getSerialPk().getSerialNumber(), blackboxVideoMetadata.getLatitude(),blackboxVideoMetadata.getLongitude(), reportName, reportContent,date,state,rejectionReason);
            } else {
                return null;
            }
        } catch (Exception e1) {
            return null;
        }
    }

    public List<AccidentReportJoinMetaDataDto> getAccident(Long userPk) {
        try {
            User user = userRepository.findById(userPk).orElse(null);
            if (user != null) {
                List<AccidentReportJoinMetaDataDto> reports = accidentReportRepository.getAccidentReportInfoByUserId(userPk);
                return reports;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AccidentReportJoinMetaDataDto getAccidentReportPk(Long reportPk) {
        try {
            AccidentReportJoinMetaDataDto reports = accidentReportRepository.getAccidentReportInfoByReportPk(reportPk);
            return reports;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<AccidentReportJoinMetaDataDto> getState(String nowState){
        try{
            List<AccidentReportJoinMetaDataDto> accidentReportJoinMetaDataDto;
            if(nowState.equals("미확인")){
                accidentReportJoinMetaDataDto = accidentReportRepository.getAccidentReportInfoByNoCheck("미확인");
            }
            else{
                accidentReportJoinMetaDataDto =  accidentReportRepository.getAccidentReportInfoByCheck("미확인");
            }
            return accidentReportJoinMetaDataDto;
        }catch (Exception e){
            return null;
        }
    }

    public List<AccidentReportJoinMetaDataDto> getDate(Date targetDate, String state){
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(targetDate); 
            int year = calendar.get(Calendar.YEAR); 
            int month = calendar.get(Calendar.MONTH) + 1; 
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            List<AccidentReportJoinMetaDataDto> accidentReportDto = null;
            if(state.equals("미확인")){
                accidentReportDto = accidentReportRepository.getAccidentReportInfoByDateNoCheck(state, year,month,day);
            }
            else if(state.equals("반려") || state.equals("보상완료")){
                accidentReportDto = accidentReportRepository.getAccidentReportInfoByDateYesCheck("미확인", year,month,day);
            }
            return accidentReportDto;
        }catch (Exception e){
            return null;
        }
    }

    public String changeState(AccidentReportDto data){
        try{
            long nowAccidentReportPk = data.getReportPk();
            String changeState = data.getState();
            if(changeState.equals("반려")){
                String rejectReason = data.getRejectionReason();
                accidentReportRepository.updateState(nowAccidentReportPk,changeState,rejectReason);
            }
            else{
                accidentReportRepository.updateState(nowAccidentReportPk,changeState,null);
            }
            return changeState;
        }catch (Exception e){
            return null;
        }
    }

    public List<PotholeDto> getPotholeList(Long videoPk){
        try{
            List<PotholeDto> potholeList = potholeRepository.findPothlesbyVideoPk(videoPk);
            return potholeList;
        }catch (Exception e){
            return null;
        }
    }
}
