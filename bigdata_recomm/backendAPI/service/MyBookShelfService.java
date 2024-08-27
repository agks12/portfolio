package com.example.pppp.Bookfolder.service;

import org.springframework.transaction.annotation.Transactional;
import com.example.pppp.Bookfolder.respository.MybookShelfRepository;
import com.example.pppp.Bookfolder.respository.MyBookListRepository;
import com.example.pppp.Bookfolder.entity.MybookShelf;
import com.example.pppp.Bookfolder.entity.MyBookList;
import com.example.pppp.Bookfolder.dto.response.MyBookShelfResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyBookShelfService {

    private final MybookShelfRepository mybookShelfRepository;
    private final MyBookListRepository myBookListRepository;


    @Autowired
    public MyBookShelfService(MybookShelfRepository mybookShelfRepository, MyBookListRepository myBookListRepository) {
        this.mybookShelfRepository = mybookShelfRepository;
        this.myBookListRepository = myBookListRepository;
    }



    public List<MyBookShelfResponseDto> getAllMybookShelves() {
        List<MybookShelf> mybookShelves = mybookShelfRepository.findAll();
        List<MyBookShelfResponseDto> mybookShelfDTOs = new ArrayList<>();
        for (MybookShelf mybookShelf : mybookShelves) {
            MyBookShelfResponseDto mybookShelfDTO = convertToDTO(mybookShelf);
            mybookShelfDTOs.add(mybookShelfDTO);
        }
        return mybookShelfDTOs;
    }


    //# 독후감 조회================================
    public List<Object> getAllMybookShelvesByMemberId(int memberId, int bookId) {
        // 회원의 독후감 목록을 조회합니다.
        List<MybookShelf> allMybookShelves = mybookShelfRepository.findAllByMemberId(memberId);

        // 독후감 목록과 책 목록을 담을 리스트를 초기화합니다.
        List<Object> result = new ArrayList<>();

        // 회원의 독후감 목록을 조회하면서 해당 책의 독후감 내용을 가져옵니다.
// 회원의 독후감 목록을 조회하면서 해당 책의 독후감 내용을 가져옵니다.
        List<Map<String, Object>> reportContents = new ArrayList<>();
        List<Map<String, Object>> reportTimes = new ArrayList<>();
        for (MybookShelf mybookShelf : allMybookShelves) {
            if (mybookShelf.getBookId() == bookId) {
                // 책의 독후감 내용을 Map에 추가합니다.
                Map<String, Object> report = new HashMap<>();
                report.put("reportContent", mybookShelf.getReportContent());
                reportContents.add(report);

                // 책의 독후감 시간을 Map에 추가합니다.
                Map<String, Object> time = new HashMap<>();
                time.put("reportTime", mybookShelf.getReportTime());
                reportTimes.add(time);
            }
        }

        // 해당 책의 정보를 조회합니다.
        MyBookList book = myBookListRepository.findByBookId(bookId);
        if (book != null) {
            // 책의 정보를 결과 리스트에 추가합니다.
            result.add(book);
        }

        // 결과 리스트의 첫 번째 인덱스에 독후감 내용과 독후감 시간을 추가합니다.
        if (!reportContents.isEmpty() && !reportTimes.isEmpty()) {
            Map<String, Object> firstItem = new HashMap<>();
            firstItem.put("reportContent", reportContents.get(0).get("reportContent"));
            firstItem.put("reportTime", reportTimes.get(0).get("reportTime"));
            result.add(0, firstItem);
        }

        return result;
    }
    //#============================================================



    //나만의 책장 책 조회-------------------------------------------
    public List<MyBookList> findBooksByMemberId(int memberId) {
        // memberId에 해당하는 bookId를 중복 없이 추출
        Set<Integer> bookIds = new HashSet<>();
        List<MybookShelf> mybookShelves = mybookShelfRepository.findAllByMemberId(memberId);
        for (MybookShelf mybookShelf : mybookShelves) {
            bookIds.add(mybookShelf.getBookId());
        }

        // 추출된 bookId에 해당하는 책들을 찾아서 반환
        List<MyBookList> books = new ArrayList<>();
        for (Integer bookId : bookIds) {
            MyBookList book = myBookListRepository.findByBookId(bookId);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }
    //=---------------------------------------------------------------------------

    //독후감 추가===================================================================
    public MybookShelf addNewMybookShelf(int memberId, int bookId, String reportContent, LocalDate reportTime, String isbn, Integer bookMemberRate) {
        MybookShelf newMybookShelf = new MybookShelf();
        newMybookShelf.setMemberId(memberId);
        newMybookShelf.setBookId(bookId);
        newMybookShelf.setReportContent(reportContent);
        newMybookShelf.setReportTime(reportTime);
        newMybookShelf.setIsbn(isbn);
        newMybookShelf.setBookMemberRate(bookMemberRate);

        return mybookShelfRepository.save(newMybookShelf);
    }
    //#=================================================================


    //독후감 삭제 동시만족==================================
    @Transactional
    public void deleteMybookShelvesByMemberIdAndBookId(int memberId, int bookId) {
        MybookShelf mybookShelf = mybookShelfRepository.findByMemberIdAndBookId(memberId, bookId);
        if (mybookShelf != null) {
            mybookShelfRepository.delete(mybookShelf);
        }
    }


//    public void deleteMybookShelvesByMemberIdAndBookId(int memberId, int bookId) {
//        mybookShelfRepository.deleteByMemberIdAndBookId(memberId, bookId);
//        try {
//            mybookShelfRepository.deleteByMemberIdAndBookId(memberId, bookId);
//            System.out.println("삭제 성공: memberId=" + memberId + ", bookId=" + bookId);
//        } catch (Exception e) {
//            System.out.println("삭제 실패: memberId=" + memberId + ", bookId=" + bookId);
//            e.printStackTrace(); // 실패 시 스택 트레이스 출력
//        }
//    }
    //=============================================


    //# 먼지 모르겠지만 DTO관련
    private MyBookShelfResponseDto convertToDTO(MybookShelf mybookShelf) {
        MyBookShelfResponseDto mybookShelfDTO = new MyBookShelfResponseDto();
        mybookShelfDTO.setMemberId(mybookShelf.getMemberId());
        mybookShelfDTO.setBookId(mybookShelf.getBookId());
        mybookShelfDTO.setReportContent(mybookShelf.getReportContent());
        mybookShelfDTO.setReportTime(mybookShelf.getReportTime());
        mybookShelfDTO.setIsbn(mybookShelf.getIsbn());
        mybookShelfDTO.setBookMemberRate(mybookShelf.getBookMemberRate());
        return mybookShelfDTO;
    }
    //================================================

}


