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
@Table(name = "book_report")
public class MybookShelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;

    @Column(name = "member_id", nullable = false)
    private int memberId;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent;

    @Column(name = "report_time")
    private LocalDate reportTime;

    @Column(name = "isbn", length = 14, nullable = false)
    private String isbn;

    @Column(name = "book_member_rate")
    private Integer bookMemberRate;

    // Getters and setters
}
