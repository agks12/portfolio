package com.h2o.poppy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o.poppy.entity.Pothole;
import com.h2o.poppy.model.pothole.PotholeDto;
import com.h2o.poppy.repository.PotholeRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PotholeService {

    private final PotholeRepository potholeRepository;

    private PotholeDto convertToDto(Pothole pothole) {
        PotholeDto potholeDto = new PotholeDto();
        potholeDto.setPotholePk(pothole.getPotholePk());
        potholeDto.setLatitude(pothole.getLocation().getCoordinate().getX());
        potholeDto.setLongitude(pothole.getLocation().getCoordinate().getY());
        potholeDto.setIsPothole(pothole.getIsPothole());
        potholeDto.setProvince(pothole.getProvince());
        potholeDto.setCity(pothole.getCity());
        potholeDto.setStreet(pothole.getStreet());
        potholeDto.setDetectedAt(pothole.getDetectedAt());
        potholeDto.setState(pothole.getState());
        potholeDto.setStartAt(pothole.getStartAt());
        potholeDto.setExpectAt(pothole.getExpectAt());
        potholeDto.setEndAt(pothole.getEndAt());
        return potholeDto;
    }

    @Autowired
    public PotholeService(PotholeRepository potholeRepository) {
        this.potholeRepository = potholeRepository;
    }

    public String callTmapApi(String lat, String lon) {
        try{
            String url = "https://apis.openapi.sk.com/tmap/road/nearToRoad";

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("version", "1")
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("opt", "0")
                    .queryParam("vehicleWidth", 250)
                    .queryParam("vehicleHeight", 340)
                    .queryParam("vehicleWeight", 35500)
                    .queryParam("vehicleTotalWeight", 26000)
                    .queryParam("vehicleLength", 880)
                    .queryParam("vehicleType", 0)
                    .queryParam("radius", 100);

            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("appKey", "ew5nSZ1Mk66M0B2t7GmhDaLb5jks5Nv35LDBJ3A5");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
            String responseBody = response.getBody();

            String roadName = extractRoadName(responseBody,lat,lon);
            return roadName;
        }catch (Exception e){
            return null;
        }

    }


    private String extractRoadName(String jsonString, String lat, String lon) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            JsonNode resultData = jsonNode.get("resultData");
            JsonNode header = resultData.get("header");
            String roadName = header.get("roadName").asText();
            String extraName = findFullRoadName(roadName, lat, lon);
            return extraName;
        } catch (Exception e) {
            return null;
        }
    }


    public String findFullRoadName(String args, String lat, String lon) {
        try{
            String url = "https://apis.openapi.sk.com/tmap/pois?version=1&searchKeyword=" + args + "&searchType=all&searchtypCd=R&centerLon=" + lon + "&centerLat=" + lat + "&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&radius=1&page=1&count=20&multiPoint=N&poiGroupYn=N";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("appKey", "ew5nSZ1Mk66M0B2t7GmhDaLb5jks5Nv35LDBJ3A5");
            HttpEntity<?> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            HttpStatusCode statusCode = response.getStatusCode();
            HttpHeaders responseHeaders = response.getHeaders();
            String responseBody = response.getBody();

            String fullName = extractJson(responseBody, args, lat, lon);

            return fullName;
        }catch (Exception e){
            return null;
        }


    }

    private String extractJson(String jsonString, String lowerAddrName, String lat, String lon) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            JsonNode resultData = jsonNode.get("searchPoiInfo");
            JsonNode pois = resultData.get("pois");
            JsonNode poi = pois.get("poi");
            JsonNode poiList = poi.get(0);
            String upperAddrName = poiList.get("upperAddrName").asText();
            String middleAddrName = poiList.get("middleAddrName").asText();

            return upperAddrName + ' ' + middleAddrName + ' ' + lowerAddrName;
        } catch (Exception e) {
            return null;
        }
    }


    public String saveData(String upperAddrName, String middleAddrName, String lowerAddrName, String lat, String lon ) {
        try{
            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat))); // x와 y는 좌표값

            Pothole pothole = new Pothole();
            pothole.setLocation(point);
            pothole.setIsPothole(true);
            pothole.setProvince(upperAddrName);
            pothole.setCity(middleAddrName);
            pothole.setStreet(lowerAddrName);
            pothole.setDetectedAt(new Date());
            pothole.setState("미확인");
            potholeRepository.save(pothole);
            long nowPk = pothole.getPotholePk();

            if(nowPk!=0)return String.valueOf(nowPk);
            else return null;
        }
        catch (Exception e){
            return null;
        }
    }

    public Pothole saveDataByUser(String upperAddrName, String middleAddrName, String lowerAddrName, String lat, String lon, String content ) {
        try{
            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat))); // x와 y는 좌표값

            Pothole pothole = new Pothole();
            pothole.setLocation(point);
            pothole.setIsPothole(false);
            pothole.setProvince(upperAddrName);
            pothole.setCity(middleAddrName);
            pothole.setStreet(lowerAddrName);
            pothole.setDetectedAt(new Date());
            pothole.setState("확인전");
            pothole.setContent(content);
            potholeRepository.save(pothole);
            long nowPk = pothole.getPotholePk();

            if(nowPk!=0)return pothole;
            else return null;
        }
        catch (Exception e){
            return null;
        }
    }

    public String changeStateByUser(Long potholePk, String nowState, String changeState){
        try{
            String returenString = null;
            if(nowState.equals("확인전")){
                if(changeState.equals("삭제")){
                    potholeRepository.deleteById(potholePk);
                    returenString = "삭제";
                }else if(changeState.equals("확인중")){
                    potholeRepository.updateByUserPotholeRejectOrCheck(potholePk,"확인중");
                    returenString = "확인중";
                }
            }else if(nowState.equals("확인중")){
                if(changeState.equals("반려")){
                    potholeRepository.updateByUserPotholeRejectOrCheck(potholePk,"반려");
                    returenString = "반려";
                }else if(changeState.equals("공사중")){

                    LocalDate now = LocalDate.now();
                    Instant instantNow = now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

                    Date currentDate = Date.from(instantNow);
                    Random random = new Random();
                    int daysToAdd = random.nextInt(4) + 2;
                    LocalDate exLocalDate = now.plusDays(daysToAdd);

                    Instant instant = exLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    Date exDate = Date.from(instant);


                    potholeRepository.updateByUserPotholeIng(potholePk,currentDate,exDate);
                    returenString = "공사중";
                }
            }
            return returenString;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<PotholeDto> getPotholesByUserUpload(){
        try{
            List<PotholeDto> potholes = potholeRepository.getPotholesByUserUpload();
            return potholes;
        }catch (Exception e){
            return null;
        }
    }

    public PotholeDto getPotholeByUserUploadOne(Long potholePk){
        try{
            PotholeDto potholes = potholeRepository.getPotholeByUserUploadOne(potholePk);
            return potholes;
        }catch (Exception e){
            return null;
        }
    }

    public boolean checkGPSdata(double lat, double lon){

        List<Pothole> potholes = potholeRepository.findNearbyPotholes(lat,lon);
        if(potholes.isEmpty())return true;
        else return false;
    }


    public List<PotholeDto> getAllPothole() {
        try {
            List<Pothole> getPothole = potholeRepository.findAll();
            return getPothole.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }catch (Exception e){
            return null;
        }
    }

    public List<PotholeDto> chooseGet(PotholeDto data){
        try {
            String nowState = data.getState();
            String nowProvince = data.getProvince();
            String nowCity = data.getCity();
            Date nowDate = data.getDetectedAt();

            Integer year = null;
            Integer month = null;
            Integer day = null;

            if(nowDate!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nowDate);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            List<PotholeDto> pothole = potholeRepository.getPotholeByFilter(nowState, nowProvince, nowCity, year,month,day);
            return pothole;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public PotholeDto getIdPothole(Long potholePk) {
        try{
            PotholeDto potholeDto = potholeRepository.getPotholeByPotholeId(potholePk);
            return potholeDto;
        }catch (Exception e){
            return null;
        }

    }

    public List<PotholeDto> getState1Pothole(String nowState){
        try{
            List<PotholeDto> statePotholes = potholeRepository.getPotholeByNowState(nowState);
            System.out.println(statePotholes);
            return statePotholes;
        }catch (Exception e){
            return null;
        }
    }

    public String changeState(PotholeDto data){
        try{
            long potholePk = data.getPotholePk();
            String nowState = data.getState();

            LocalDate now = LocalDate.now();
            Instant instantNow = now.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

            Date currentDate = Date.from(instantNow);
            Random random = new Random();
            int daysToAdd = random.nextInt(4) + 2;
            LocalDate exLocalDate = now.plusDays(daysToAdd);

            Instant instant = exLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date exDate = Date.from(instant);

            if(nowState==null)return null;
            if(!nowState.equals("미확인") && !nowState.equals("공사중") && !nowState.equals("공사완료"))return null;
            String changeState =null;
            if(nowState.equals("공사중")){
                potholeRepository.updateIngState(potholePk,"공사중",currentDate,exDate);
                changeState = "공사중";
            }else if(nowState.equals("공사완료")){
                potholeRepository.updateFnishState(potholePk,"공사완료",currentDate);
                changeState = "공사완료";
            }
            return changeState;
        }catch (Exception e){
            return null;
        }
    }


    public boolean rejectData(Long potholePk) {
        try {
            potholeRepository.updateIsPothole(potholePk);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<PotholeDto> getBoundary(double targetLatitude,double targetLongitude, double size){
        try{
            List<PotholeDto> pothole = potholeRepository.findPothlesbySize(targetLatitude,targetLongitude,size);
            return pothole;
        }catch (Exception e){
            return null;
        }
    }


    public List<PotholeDto> getTraceSearch(double targetLatitude, double targetLongitude){
        try{
            List<PotholeDto> potholes = potholeRepository.findPothlesbyTrace(targetLatitude,targetLongitude);

            if (potholes.isEmpty()) {
                return new ArrayList<>();
            } else {
                return potholes;
            }
        }catch (Exception e){
            return null;
        }
    }

    public Long deletePothole(Long potholePk){
        try{
            potholeRepository.deleteById(potholePk);
            return potholePk;
        }catch (Exception e){
            return 0L;
        }
    }

}
