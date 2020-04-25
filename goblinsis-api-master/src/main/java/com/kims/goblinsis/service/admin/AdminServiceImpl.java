package com.kims.goblinsis.service.admin;

import com.kims.goblinsis.model.domain.user.Role;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.repository.user.RoleRepository;
import com.kims.goblinsis.repository.user.UserRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.user.UserService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl extends CommonService implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<User> getInfoList(String roleName, Pageable pageable) {
        try {
            if (roleName.equalsIgnoreCase("ALL")) {
                List<Role> roleList = roleRepository.findAll();
                return userRepository.findAllByRoleIn(roleList, pageable);
            } else {
                List<Role> roleList = roleRepository.findAllByRolename(roleName);
                return userRepository.findAllByRoleIn(roleList, pageable);
            }
        } catch (Exception e) {
            logger.error("AdminService -> getInfoList : " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean kickOutUser(int userId) {
        try {
            String[] roles = {Constants.ROLE_OUT};
            List<Role> roleList = roleRepository.findAllByRolenameNotIn(roles);
            User user = userRepository.findByIdAndRoleIn(userId, roleList);

            if (user != null) {
                Role role = user.getRole();
                role.setRolename(Constants.ROLE_OUT);
                roleRepository.save(role);
                return true;
            }
        } catch (Exception e) {
            logger.error("AdminService -> kickOutUser : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean userConvertToAdmin(int userId) {
        User user = userService.findById(userId);

        try {
            if (user != null) {
                Role role = user.getRole();
                role.setRolename(Constants.ROLE_ADMIN);
                roleRepository.save(role);
                return true;
            }
        } catch (Exception e) {
            logger.error("AdminService -> userConvertToAdmin : " + e.getMessage());
        }

        return false;
    }

}
