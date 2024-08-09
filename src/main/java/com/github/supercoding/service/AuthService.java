package com.github.supercoding.service;

import com.github.supercoding.repository.roles.Roles;
import com.github.supercoding.repository.roles.RolesRepository;
import com.github.supercoding.repository.userPrincipal.UserPrincipal;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRepository;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRoles;
import com.github.supercoding.repository.userPrincipal.UserPrincipalRolesRepository;
import com.github.supercoding.repository.users.UserEntity;
import com.github.supercoding.repository.users.UserRepository;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserPrincipalRepository userPrincipalRepository;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;

    private final PasswordEncoder passwordEncoder; // 패스워드 인코딩용

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
}
