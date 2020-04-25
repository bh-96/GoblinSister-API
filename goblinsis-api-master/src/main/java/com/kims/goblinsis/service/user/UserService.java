package com.kims.goblinsis.service.user;

import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.UserDTO;

public interface UserService extends org.springframework.security.core.userdetails.UserDetailsService {

    User findByAccount(String account);

    User createUser(UserDTO userDTO);

    int checkPhoneValid(String phone);

    User save(User user);

    boolean checkPw(String password, String encodedPw);

    boolean updatePw(User user, String newPassword);

    User updateInfo(User user, UserDTO userDTO);

    boolean delete(User user, String password);

    int findIdByAccount(String account);

    User findById(int id);

    String findAccountByNameAndEmail(String name, String email);

    String findPwAndUpdateRandomPw(String account, String name, String email);

}