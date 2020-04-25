package com.kims.goblinsis.security;

import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.user.UserService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class TokenAuthenticationService extends CommonService {

    @Autowired
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    private UserService userService;

    public void addJwtTokenToHeader(HttpServletResponse response,
                                    UserAuthentication authentication) {
        final UserDetails user = authentication.getDetails();

        logger.info("login user account : " + user.getUsername());

        // header 에 role 추가
        User userAccount = userService.findByAccount(user.getUsername());
        if (userAccount != null) {
            response.addHeader(Constants.AUTH_HEADER_ROLE, userAccount.getRole().getRolename());
        }

        // 강퇴 당한 회원에게는 토큰 값 전달하지 않음
        if (!userAccount.getRole().getRolename().equalsIgnoreCase(Constants.ROLE_OUT)) {
            response.addHeader(Constants.AUTH_HEADER_TOKEN, jwtTokenHandler.createTokenForUser(user));
        }
    }

    public Authentication generateAuthenticationFromRequest(HttpServletRequest request) {
        final String token = request.getHeader(Constants.AUTH_HEADER_TOKEN);
        if (token == null || token.isEmpty()) return null;
        return jwtTokenHandler
                .parseUserFromToken(token)
                .map(UserAuthentication::new)
                .orElse(null);
    }
}
