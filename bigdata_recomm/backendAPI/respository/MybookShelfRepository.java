package com.example.pppp.Bookfolder.respository;

import com.example.pppp.Bookfolder.entity.MybookShelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MybookShelfRepository extends JpaRepository<MybookShelf, Integer> {
    List<MybookShelf> findAllByMemberId(int memberId); //모두 가져오기?
    List<Object[]> findBookNameAndReportContentAndReportTimeByMemberIdAndBookId(int memberId, int bookId);

    MybookShelf findByMemberIdAndBookId(int memberId, int bookId); //삭제함수
}

