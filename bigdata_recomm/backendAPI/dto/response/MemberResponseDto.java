package com.example.pppp.Bookfolder.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private String nickname;

    private String password;

    private String email;

    private int memberGrade;

    private int sex;

    private String school;
}
