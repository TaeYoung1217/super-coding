package com.github.supercoding.service;

import com.github.supercoding.config.security.JwtTokenProvider;
import com.github.supercoding.repository.roles.Roles;
import com.github.supercoding.repository.roles.RolesRepository;
import com.github.supercoding.repository.userPrincipal.UserPrincipal;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRepository;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRoles;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRolesRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.repository.users.UserRepository;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.auth.Login;
import com.github.supercoding.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserPrincipalRepository userPrincipalRepository;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;
    private final PasswordEncoder passwordEncoder; // 패스워드 인코딩용
    private final AuthenticationManager authenticationManager;

    @Transactional(transactionManager = "tmJpa2")
    public boolean signUp(SignUp signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String userName = signUpRequest.getName();

        //존재하는 이메일이면 false return
        if(userPrincipalRepository.existsByEmail(email)){
            return false;
        }

        //유저가 있으면 userentity 만들기
        UserEntity userFound = userRepository.findByUserName(userName)
                .orElseGet(()->userRepository.save(UserEntity.builder()
                        .userName(userName)
                        .likeTravelPlace("파리")
                        .phoneNum("+82-000-0000")
                        .build()));

        //username, password 등록, 기본 ROLE_USER
        Roles roles = rolesRepository.findByName("ROLE_USER")
                .orElseThrow(()->new NotFoundException("ROLE_USER를 찾을 수 없습니다"));

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .email(email)
                .user(userFound)
                .password(passwordEncoder.encode(password)) //password 인코딩 필요
                .build();

        userPrincipalRepository.save(userPrincipal);
        userPrincipalRolesRepository.save(UserPrincipalRoles.builder()
                .roles(roles)
                .userPrincipal(userPrincipal)
                .build());

        return true;
    }

    public String login(Login loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = userPrincipalRepository.findByEmailFetchJoin(email)
                    .orElseThrow(()->new NotFoundException("USER_NOT_FOUND"));
            List<String> roles = userPrincipal.getUserPrincipalRoles()
                    .stream()
                    .map(UserPrincipalRoles::getRoles)
                    .map(Roles::getName)
                    .toList();
            return jwtTokenProvider.createToken(email, roles);
        }catch (Exception e){
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }

    }
}
