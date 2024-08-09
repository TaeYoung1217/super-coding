package com.github.supercoding.web.controller;

import com.github.supercoding.service.AuthService;
import com.github.supercoding.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/sign")
public class SignController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody SignUp signUpRequest) {
        boolean isSuccess = authService.signUp(signUpRequest);
        return isSuccess ? "회원가입에 성공" : "회원가입에 실패";
    }
}
