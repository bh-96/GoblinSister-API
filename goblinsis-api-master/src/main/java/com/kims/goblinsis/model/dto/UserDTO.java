package com.kims.goblinsis.model.dto;

import com.kims.goblinsis.utils.Constants;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Data
public final class UserDTO {

    private String account;         // 사용자 아이디
    private String password;        // 기존 비밀번호
    private String newPassword;     // 변경할 비밀번호
    private String name;            // 사용자 이름
    private String birth;           // 사용자 생년월일 (yyyy-MM-dd)
    private String phone;           // 사용자 연락처
    private String address;         // 사용자 주소
    private String email;           // 사용자 이메일
    private String gender;          // 사용자 성별

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(account, password, getAuthorities());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> Constants.ROLE_USER);
    }

}