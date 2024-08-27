package com.example.pppp.Bookfolder.dto.response;

import java.time.LocalDate;



public class MyBookShelfResponseDto {

    private int memberId;
    private int bookId;
    private String reportContent;
    private LocalDate reportTime;
    private String isbn;
    private Integer bookMemberRate;

    // 생성자, Getter 및 Setter 메서드
    // 생략

    // memberId 속성에 대한 getter 메서드
    public int getMemberId() {
        return memberId;
    }

    // memberId 속성에 대한 setter 메서드
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    // bookId 속성에 대한 getter 메서드
    public int getBookId() {
        return bookId;
    }

    // bookId 속성에 대한 setter 메서드
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    // reportContent 속성에 대한 getter 메서드
    public String getReportContent() {
        return reportContent;
    }

    // reportContent 속성에 대한 setter 메서드
    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    // reportTime 속성에 대한 getter 메서드
    public LocalDate getReportTime() {
        return reportTime;
    }

    // reportTime 속성에 대한 setter 메서드
    public void setReportTime(LocalDate reportTime) {
        this.reportTime = reportTime;
    }

    // isbn 속성에 대한 getter 메서드
    public String getIsbn() {
        return isbn;
    }

    // isbn 속성에 대한 setter 메서드
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // bookMemberRate 속성에 대한 getter 메서드
    public Integer getBookMemberRate() {
        return bookMemberRate;
    }

    // bookMemberRate 속성에 대한 setter 메서드
    public void setBookMemberRate(Integer bookMemberRate) {
        this.bookMemberRate = bookMemberRate;
    }
}
