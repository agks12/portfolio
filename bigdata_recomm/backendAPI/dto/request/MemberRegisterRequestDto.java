package com.example.pppp.Bookfolder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterRequestDto {
    private String nickname;

    private String password;

    private String email;

    private int memberGrade;

    private int sex;

    private String school;

}
