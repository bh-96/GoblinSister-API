package com.kims.goblinsis.service;

import com.kims.goblinsis.model.domain.file.UploadFile;
import com.kims.goblinsis.model.dto.UserDTO;
import com.kims.goblinsis.utils.Constants;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonService {

    protected Logger logger = LoggerFactory.getLogger(getClass().getName());

    // json 에러 메시지 생성
    public JSONObject returnErrJsonObj(String msg) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("result", false);
        jsonObj.put("e_message", msg);

        return jsonObj;
    }

    // userDTO 에서 null 값인 변수는 "" 으로 처리
    public UserDTO nullConvertToBlank(UserDTO userDTO) {
        UserDTO returnDTO = new UserDTO();

        String account = userDTO.getAccount() != null ? userDTO.getAccount() : "";
        String password = userDTO.getPassword() != null ? userDTO.getPassword() : "";
        String newPassword = userDTO.getNewPassword() != null ? userDTO.getNewPassword() : "";
        String name = userDTO.getName() != null ? userDTO.getName() : "";
        String birth = userDTO.getBirth() != null ? userDTO.getBirth() : "";
        String phone = userDTO.getPhone() != null ? userDTO.getPhone() : "";
        String address = userDTO.getAddress() != null ? userDTO.getAddress() : "";
        String email = userDTO.getEmail() != null ? userDTO.getEmail() : "";
        String gender = userDTO.getGender() != null ? userDTO.getGender() : "";

        // 특수문자, 공백 제거
        phone = phone.replaceAll("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]", "").replaceAll(" ", "").trim();

        returnDTO.setAccount(account);
        returnDTO.setPassword(password);
        returnDTO.setNewPassword(newPassword);
        returnDTO.setName(name);
        returnDTO.setBirth(birth);
        returnDTO.setAddress(address);
        returnDTO.setEmail(email);
        returnDTO.setGender(gender);

        // 휴대폰 번호가 10자리 또는 11자리가 아니거나 '-' 를 제외한 번호가 숫자가 아닐 때
        if (!(phone.length() == 10 || phone.length() == 11) || stringToInt(phone) == 0) {
            returnDTO.setPhone("");
        } else {
            returnDTO.setPhone(phone);
        }

        return returnDTO;
    }

    public int stringToInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            return 0;
        }
    }

    // 글 내용에서 파일 index 가져오기
    public int[] findFileIdByContent(String content) {
        String[] contents = content.split(Constants.API_FILE);

        if (contents.length > 1) {
            int[] files = new int[contents.length];

            for (int i = 0; i < contents.length; i++) {
                String c = contents[i];

                if (c.charAt(0) == '/') {
                    try {
                        int fileId = c.substring(1, c.indexOf("\"")).length() > 0 ? Integer.parseInt(c.substring(1, c.indexOf("\""))) : 0;
                        files[i] = fileId;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return files;
        }

        return null;
    }

    // 파일 index 찾기
    public int[] findFileId(List<UploadFile> files) {
        int[] ids = new int[files.size()];

        for (int i = 0; i < files.size(); i++) {
            ids[i] = files.get(i).getId();
        }

        return ids;
    }
}
