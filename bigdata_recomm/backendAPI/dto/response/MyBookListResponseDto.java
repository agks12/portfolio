package com.example.pppp.Bookfolder.dto.response;

import java.time.LocalDate;

public class MyBookListResponseDto {

    private int bookId;
    private String isbn;
    private String bookName;
    private String grade;
    private int schoolBookGrade;
    private int bookCategory;
    private Integer series;
    private Integer numberOfPages;
    private String author;
    private String publisher;
    private LocalDate publisherDate;
    private String imgUrl;
    private String description;
    private Double score;

    public MyBookListResponseDto() {
    }

    public MyBookListResponseDto(int bookId, String isbn, String bookName, String grade, int schoolBookGrade, int bookCategory, Integer series, Integer numberOfPages, String author, String publisher, LocalDate publisherDate, String imgUrl, String description, Double score) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.bookName = bookName;
        this.grade = grade;
        this.schoolBookGrade = schoolBookGrade;
        this.bookCategory = bookCategory;
        this.series = series;
        this.numberOfPages = numberOfPages;
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.imgUrl = imgUrl;
        this.description = description;
        this.score = score;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getSchoolBookGrade() {
        return schoolBookGrade;
    }

    public void setSchoolBookGrade(int schoolBookGrade) {
        this.schoolBookGrade = schoolBookGrade;
    }

    public int getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(int bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublisherDate() {
        return publisherDate;
    }

    public void setPublisherDate(LocalDate publisherDate) {
        this.publisherDate = publisherDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
