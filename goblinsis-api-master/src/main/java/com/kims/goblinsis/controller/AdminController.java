package com.kims.goblinsis.controller;

import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.admin.AdminService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_ADMIN)
public class AdminController extends CommonService {

    @Autowired
    private AdminService adminService;

    /**
     * 회원 목록 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> userInfo(@RequestParam(value = "roleName", defaultValue = "ALL") String roleName, Pageable pageable) {
        return new ResponseEntity<>(adminService.getInfoList(roleName, pageable), HttpStatus.OK);
    }

    /**
     * 회원 강퇴
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> kickOutUser(@RequestParam int userId) {
        return new ResponseEntity<>(adminService.kickOutUser(userId), HttpStatus.OK);
    }

    /**
     * 일반 사용자 관리자로 전환
     */
    @RequestMapping(value = "/convert", method = RequestMethod.PUT)
    public ResponseEntity<?> userConvertToAdmin(@RequestParam(value = "userId") int userId) {
        return new ResponseEntity<>(adminService.userConvertToAdmin(userId), HttpStatus.OK);
    }

}
