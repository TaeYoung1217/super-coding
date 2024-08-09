package com.github.supercoding.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final String secretKey = Base64.getEncoder()
                                            .encodeToString("super-coding".getBytes());
    private long tokenValidMilliSeconds = 1000L * 60 * 60;

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
                .setExpiration(new Date(now.getTime() - tokenValidMilliSeconds))
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
