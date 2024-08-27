package com.example.pppp.Bookfolder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class MyBookList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "ISBN", length = 13, nullable = false)
    private String isbn;

    @Column(name = "book_Name", length = 200, nullable = false)
    private String bookName;

    @Column(name = "grade", length = 2)
    private String grade;

    @Column(name = "school_book_grade", nullable = false)
    private int schoolBookGrade;

    @Column(name = "book_category", nullable = false)
    private int bookCategory;

    @Column(name = "series")
    private Integer series;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "author", length = 50)
    private String author;

    @Column(name = "publisher", length = 25)
    private String publisher;

    @Column(name = "publisher_date")
    private LocalDate publisherDate;

    @Column(name = "img_url", length = 255)
    private String imgUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "score", precision = 4)
    private Double score;
}
