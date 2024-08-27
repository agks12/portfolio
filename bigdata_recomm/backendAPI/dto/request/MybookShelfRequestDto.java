package com.example.pppp.Bookfolder.dto.request;
import java.time.LocalDate;

public class MybookShelfRequestDto {

    private int memberId;
    private int bookId;
    private String reportContent;
    private LocalDate reportTime;
    private String isbn;
    private Integer bookMemberRate;

    // 생성자, 게터, 세터 등 필요한 메서드 추가

    // 생성자
    public MybookShelfRequestDto(int memberId, int bookId, String reportContent, LocalDate reportTime, String isbn, Integer bookMemberRate) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.reportContent = reportContent;
        this.reportTime = reportTime;
        this.isbn = isbn;
        this.bookMemberRate = bookMemberRate;
    }

    // 게터, 세터
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public LocalDate getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDate reportTime) {
        this.reportTime = reportTime;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getBookMemberRate() {
        return bookMemberRate;
    }

    public void setBookMemberRate(Integer bookMemberRate) {
        this.bookMemberRate = bookMemberRate;
    }
}
