package com.kims.goblinsis.service.admin;

import com.kims.goblinsis.model.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<User> getInfoList(String roleName, Pageable pageable);

    // 사용자 강퇴
    boolean kickOutUser(int userId);

    // 일반 사용자에게 관리자 권한 부여
    boolean userConvertToAdmin(int userId);

}
