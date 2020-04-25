package com.kims.goblinsis.security;

import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccountChecker {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private UserService userService;

    public User getUser(HttpServletRequest request) {
        String account = tokenAuthenticationService.generateAuthenticationFromRequest(request).getName();
        return userService.findByAccount(account);
    }

    public int getUserId(HttpServletRequest request) {
        String account = tokenAuthenticationService.generateAuthenticationFromRequest(request).getName();
        return userService.findIdByAccount(account);
    }
}
