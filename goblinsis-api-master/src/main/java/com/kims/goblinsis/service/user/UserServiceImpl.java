package com.kims.goblinsis.service.user;

import com.kims.goblinsis.model.domain.user.Role;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.UserDTO;
import com.kims.goblinsis.repository.user.UserRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.utils.Constants;
import com.kims.goblinsis.utils.DateFormatUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl extends CommonService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByAccount(account);
        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
    }

    @Override
    public User findByAccount(String account) {
        try {
            return userRepository.findByAccount(account).get();
        } catch (Exception e) {
            logger.error("UserService -> findByUserName : " + e.getMessage());
            return null;
        }
    }

    @Override
    public User createUser(UserDTO userDTO) {
        // role 적용
        User user = toUserRole();

        user.setAccount(userDTO.getAccount());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setBirth(DateFormatUtil.getFormatDateByString("yyyy-MM-dd", userDTO.getBirth()));
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());

        return save(user);
    }

    private User toUserRole() {
        User user = new User();
        Role role = new Role();
        role.setRolename(Constants.ROLE_USER);
        user.setRole(role);
        return user;
    }

    @Override
    public int checkPhoneValid(String phone) {
        try {
            User user = userRepository.findByPhone(phone);

            if (user != null) {
                // 강퇴 당한 회원 return true;
                if (user.getRole().getRolename().equalsIgnoreCase(Constants.ROLE_OUT)) {
                    return Constants.KICK_OUT_PHONE;
                }

                return Constants.OVERLAP_PHONE;
            }
        } catch (Exception e) {
            logger.error("UserService -> checkKickOutUser : " + e.getMessage());
        }

        return Constants.AVAILABLE_PHONE;
    }

    @Override
    public User save(User user) {
        try {
            return userRepository.saveAndFlush(user);
        } catch (Exception e) {
            logger.error("UserService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean checkPw(String password, String encodedPw) {
        try {
            if (new BCryptPasswordEncoder().matches(password, encodedPw)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("UserService -> checkPw : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean updatePw(User user, String newPassword) {
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        if (save(user) != null) {
            return true;
        }

        return false;
    }

    @Override
    public User updateInfo(User user, UserDTO userDTO) {
        boolean check = false;

        if (!userDTO.getName().equals("")) {
            user.setName(userDTO.getName());
            check = true;
        }

        if (!userDTO.getBirth().equals("")) {
            user.setBirth(DateFormatUtil.getFormatDateByString("yyyy-MM-dd", userDTO.getBirth()));
            check = true;
        }

        if (!userDTO.getPhone().equals("")) {
            user.setPhone(userDTO.getPhone());
            check = true;
        }

        if (!userDTO.getAddress().equals("")) {
            user.setAddress(userDTO.getAddress());
            check = true;
        }

        if (!userDTO.getEmail().equals("")) {
            user.setEmail(userDTO.getEmail());
            check = true;
        }

        if (!userDTO.getGender().equals("")) {
            user.setGender(userDTO.getGender());
            check = true;
        }

        if (check) {
            return save(user);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(User user, String password) {
        // 비밀번호가 맞으면 삭제 진행
        if (checkPw(password, user.getPassword())) {
            try {
                userRepository.deleteById(user.getId());
                return true;
            } catch (Exception e) {
                logger.error("UserService -> delete : " + e.getMessage());
            }
        }

        return false;
    }

    @Override
    public int findIdByAccount(String account) {
        User user = findByAccount(account);

        if (user != null) {
            return user.getId();
        }

        return 0;
    }

    @Override
    public User findById(int id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.error("UserService -> findById : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String findAccountByNameAndEmail(String name, String email) {
        try {
            User user = userRepository.findByNameAndEmail(name, email);

            if (user != null) {
                return user.getAccount();
            }
        } catch (Exception e) {
            logger.error("UserService -> findAccountByNameAndEmail : " + e.getMessage());
        }

        return null;
    }

    @Override
    public String findPwAndUpdateRandomPw(String account, String name, String email) {
        try {
            User user = userRepository.findByAccountAndNameAndEmail(account, name, email);

            if (user != null) {
                // 10자리 랜덤 알파벳 문자열 생성
                String newRandomPw = RandomStringUtils.randomAlphabetic(10);

                // 생성한 랜덤 문자열로 비밀번호 변경
                updatePw(user, newRandomPw);

                return newRandomPw;
            }
        } catch (Exception e) {
            logger.error("UserService -> findPwAndUpdateRandomPw : " + e.getMessage());
        }

        return null;
    }

}
