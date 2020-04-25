package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.UserDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.mail.MailService;
import com.kims.goblinsis.service.user.UserService;
import com.kims.goblinsis.utils.Constants;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(Constants.API_USER)
public class UserController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    /**
     * 회원가입
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        // 파라미터 유효성 체크
        JSONObject jsonObj = checkUserInfo(userDTO);

        if ((boolean) jsonObj.get("result")) {
            return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(jsonObj, HttpStatus.BAD_REQUEST);   // 400 error
        }
    }

    /**
     * 아이디 중복 체크
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<?> checkUserName(@RequestParam(value = "account", defaultValue = "") String account) {
        // 파라미터로 아이디를 입력하지 않았거나, 이미 존재하는 아이디이면 return false;
        if (account.equals("")) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByAccount(account);
        if (user == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        // 사용가능 아이디 return true;
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * 비밀번호 확인
     */
    @RequestMapping(value = "/check/pw", method = RequestMethod.GET)
    public ResponseEntity<?> checkPassword(@RequestParam(value = "password", defaultValue = "") String password, HttpServletRequest request) {
        if (password.equals("")) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 일치하면 return true;
        User user = accountChecker.getUser(request);
        if (userService.checkPw(password, user.getPassword())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    /**
     * 비밀번호 변경
     */
    @RequestMapping(value = "/pw", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePw(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = accountChecker.getUser(request);

        // 기존 비밀번호 확인
        if (!userService.checkPw(userDTO.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        // 비밀번호 변경
        return new ResponseEntity<>(userService.updatePw(user, userDTO.getNewPassword()), HttpStatus.OK);
    }

    /**
     * 회원정보 수정 (아이디, 비밀번호 제외)
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = accountChecker.getUser(request);
        userDTO = nullConvertToBlank(userDTO);
        user = userService.updateInfo(user, userDTO);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("회원정보 수정 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 회원탈퇴
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam(value = "password", defaultValue = "") String password, HttpServletRequest request) {
        if (password.equals("")) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        User user = accountChecker.getUser(request);
        return new ResponseEntity<>(userService.delete(user, password), HttpStatus.OK);
    }

    /**
     * 사용자 아이디 반환
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<?> update(@RequestParam int id) {
        User user = userService.findById(id);

        if (user != null) {
            return new ResponseEntity<>(user.getAccount(), HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("해당 인덱스의 사용자가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 아이디 찾기
     */
    @RequestMapping(value = "/find/account", method = RequestMethod.POST)
    public ResponseEntity<?> findAccountAndSendEmail(@Valid @RequestParam String name, @Valid @RequestParam String email) {
        String account = userService.findAccountByNameAndEmail(name, email);

        if (account != null) {
            String subject = "[도깨비언니] 회원정보 조회 - 아이디";
            String content = "안녕하세요. 도깨비언니입니다. <br><br>"
                    + name + " 님의 아이디는 \"" + account + "\" 입니다. <br><br>"
                    + "감사합니다~ :) <br><br>";

            return new ResponseEntity<>(mailService.sendMail(email, subject, content), HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("해당 정보로 저장된 회원정보가 없습니다."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 아이디 찾기
     */
    @RequestMapping(value = "/find/account", method = RequestMethod.GET)
    public ResponseEntity<?> findAccount(@Valid @RequestParam String name, @Valid @RequestParam String email) {
        String account = userService.findAccountByNameAndEmail(name, email);

        if (account != null) {
                        return new ResponseEntity<>(account, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("해당 정보로 저장된 회원정보가 없습니다."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 비밀번호 찾기
     */
    @RequestMapping(value = "/find/pw", method = RequestMethod.POST)
    public ResponseEntity<?> findPwAndSendEmail(@Valid @RequestParam String account, @Valid @RequestParam String name, @Valid @RequestParam String email) {
        String newRandomPw = userService.findPwAndUpdateRandomPw(account, name, email);

        if (newRandomPw != null) {
            String subject = "[도깨비언니] 회원정보 조회 - 비밀번호";
            String content = "안녕하세요. 도깨비언니입니다. <br><br>"
                    + name + " 님의 비밀번호가 변경 되었습니다. <br><br>"
                    + "\t" + newRandomPw + " <br><br>"
                    + "해당 비밀번호로 로그인 하신 뒤, 비밀번호를 변경해주세요. <br><br>"
                    + "감사합니다~ :) <br><br>";
            return new ResponseEntity<>(mailService.sendMail(email, subject, content), HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("해당 정보로 저장된 회원정보가 없습니다."), HttpStatus.BAD_REQUEST);
    }


    // 파라미터 유효성 체크
    private JSONObject checkUserInfo(UserDTO userDTO) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("result", true);

        userDTO = nullConvertToBlank(userDTO);

        // 필수 값을 입력하지 않았을 때
        if (userDTO.getAccount().equals("") || userDTO.getPassword().equals("") || userDTO.getName().equals("") || userDTO.getAddress().equals("")
                || userDTO.getEmail().equals("") || userDTO.getGender().equals("")) {
            return returnErrJsonObj("필수 값이 비어있습니다.");
        }

        // 특수문자, 공백 제거
        String checkUsername = userDTO.getAccount().replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "").replaceAll(" ", "").trim();

        // 아이디에 특수기호 또는 공백이 들어갔거나 2~10 자 사이가 아닐 때
        if (!userDTO.getAccount().equalsIgnoreCase(checkUsername) || userDTO.getAccount().length() < 2 || userDTO.getAccount().length() > 10) {
            return returnErrJsonObj("아이디에 특수문자나 공백이 포함되어 있습니다. (아이디 : 2~10 자리)");
        }

        if (userDTO.getPassword().length() < 8 || userDTO.getPassword().length() > 15) {
            return returnErrJsonObj("비밀번호는 8~15 자리 입니다.");
        }

        // 휴대폰 번호가 10자리 또는 11자리가 아니거나 '-' 를 제외한 번호가 숫자가 아닐 때
        if (userDTO.getPhone().equals("")) {
            return returnErrJsonObj("휴대폰 번호가 유효하지 않습니다.");
        }

        // 강퇴 당한 회원인지 체크
        int checkPhoneValid = userService.checkPhoneValid(userDTO.getPhone());
        if (checkPhoneValid == Constants.KICK_OUT_PHONE) {
            return returnErrJsonObj("강퇴 당한 회원입니다.");
        }

        // 휴대폰 번호 중복인지 체크
        if (checkPhoneValid == Constants.OVERLAP_PHONE) {
            return returnErrJsonObj("이미 등록된 휴대폰 번호 입니다.");
        }

        return jsonObj;
    }
}
