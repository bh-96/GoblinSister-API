package com.kims.goblinsis.config;

import com.kims.goblinsis.security.filters.StatelessAuthenticationFilter;
import com.kims.goblinsis.security.filters.StatelessLoginFilter;
import com.kims.goblinsis.security.TokenAuthenticationService;
import com.kims.goblinsis.service.user.UserService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAsync
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        //h2 database console
        http.headers().frameOptions().disable();

        http.exceptionHandling()
            .and().anonymous()
            .and().servletApi()
            .and().headers().cacheControl();

        http.authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/admin/**").hasRole(Constants.ADMIN)
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/post/**").permitAll()
                .antMatchers("/api/file/**").permitAll()
                .antMatchers("/api/comment/**").permitAll()
                .antMatchers("/api/statistics/**").hasRole(Constants.ADMIN)
                .antMatchers("/api/purchase/**").permitAll()
                .antMatchers("/api/refund/**").permitAll()
                .antMatchers("/api/review/**").permitAll()
                .antMatchers("/api/faq/**").permitAll()
                .antMatchers("/api/qna/**").permitAll()
                .antMatchers("/api/notice/**").permitAll()
                .antMatchers(HttpMethod.POST, "/console/**").permitAll();

        http.addFilterBefore(
                /**
                 * 로그인
                 */
                new StatelessLoginFilter("/api/login", tokenAuthenticationService, userService, authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(
                new StatelessAuthenticationFilter(tokenAuthenticationService),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userService;
    }
}