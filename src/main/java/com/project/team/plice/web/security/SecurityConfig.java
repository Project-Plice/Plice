package com.project.team.plice.web.security;

import com.project.team.plice.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginServiceImpl loginService;
    @Autowired
    DataSource dataSource;

    // 접근 허용 목록 (정적인 파일들)
    private static final String[] AUTH_WHITELIST = {
            "/img/**",
            "/css/**",
            "/js/**",
            "/upload-img/**",
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {  // 회원가입 시 비밀번호 암호화에 사용할 Encoder 빈 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                    // 로그인 필요 없는 페이지들 여기에 추가
                    .antMatchers("/", "/login/**", "/join/**").permitAll()

                    .antMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("phone")
                    .passwordParameter("pw")
                    .loginProcessingUrl("/loginProc")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login/error")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                .and()
                    .rememberMe()
                    .key("rememberMe")
                    .tokenValiditySeconds(3600)
                    .alwaysRemember(false)
                    .userDetailsService(loginService)
                    .tokenRepository(tokenRepository())
                .and()
                    .csrf().disable();

        //중복 로그인
        http.sessionManagement()
                .maximumSessions(1) //세션 최대 허용 수
                .maxSessionsPreventsLogin(false); // false이면 중복 로그인하면 이전 로그인이 풀린다.
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적인 파일 요청에 대해 무시
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("1234")).roles("ADMIN");
        auth
                .userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }



}