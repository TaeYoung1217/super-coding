package com.github.supercoding.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private String secretKey;

    @PostConstruct //
    public void setUp(){
        secretKey = Base64.getEncoder()
                .encodeToString(secretKeySource.getBytes());
    }

    private long tokenValidTime = 1000L * 60 * 60;

    private final UserDetailsService userDetailsService;

    //토큰에서 원하는 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    //토큰 유효성 검증
    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(jwtToken).getBody();
            Date now = new Date();
            return claims.getExpiration().after(now);
        }catch (Exception e) {
            return false;
        }
    }

    public String createToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                //24.08.09 만료기간이 잘못 설정되어 있었음. -> 현재시간-유지시간을 해버리니까 토큰 발급할때부터 만료였음
                // now.getTime() - tokenValidTime 이렇게 되어있으니까 토큰 만료시간이 과거 시점이 되어버려서 403 forbidden 에러가 발생.
                // now.getTime() + tokenValidTime 이렇게 바꿔서 토큰 만료시점을 현재시간 + 토큰 유지 시간으로 수정하여 문제 해결.
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    //
    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String getUserEmail(String jwtToken){
        return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }
}
