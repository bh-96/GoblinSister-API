package com.kims.goblinsis.repository.user;

import com.kims.goblinsis.model.domain.user.Role;
import com.kims.goblinsis.model.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAccount(String account);

    Page<User> findAllByRoleIn(List<Role> roleList, Pageable pageable);

    User findByIdAndRoleIn(int id, List<Role> roleList);

    User findByPhone(String phone);

    User findById(int id);

    User findByNameAndEmail(String name, String email);

    User findByAccountAndNameAndEmail(String account, String name, String email);
}
