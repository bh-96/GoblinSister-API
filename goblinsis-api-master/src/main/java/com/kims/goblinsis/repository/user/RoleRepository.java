package com.kims.goblinsis.repository.user;

import com.kims.goblinsis.model.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findAllByRolename(String roleName);

    List<Role> findAllByRolenameNotIn(String[] roles);
}
