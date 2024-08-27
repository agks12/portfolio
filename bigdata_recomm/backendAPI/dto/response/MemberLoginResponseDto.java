package com.example.pppp.Bookfolder.dto.response;

import com.example.pppp.Bookfolder.common.BaseResponseBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginResponseDto extends BaseResponseBody{

    private String token;

    private String nickname;


    public static MemberLoginResponseDto of(Integer statusCode, String message, String accessToken, String nickname) {
        MemberLoginResponseDto res = new MemberLoginResponseDto();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setToken(accessToken);
        res.setNickname(nickname);

        return res;
    }
}
