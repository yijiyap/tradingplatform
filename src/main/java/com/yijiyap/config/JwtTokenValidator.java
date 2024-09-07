package com.yijiyap.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            try {

//                Create a Secret key using HMAC-SHA algorithm based on our Secret.
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET.getBytes());

//                Verify JWT and get info about the user being authenticated.
                Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtToken).getPayload();

                String email = String.valueOf(claims.get("email"));

                String authorities = String.valueOf(claims.get("authorities"));

//                Defines permissions of users.
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

//                Create auth token. Use `null` for credentials as the authentication is based on JWT token.
                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);

//                Tell Spring Security that the user is authenticated.
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                throw new RuntimeException("Invalid JWT token");
            }
        }

//        Continue the filter chain after processing JWT and authentication.
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException("Error in filter chain that isn't JWT: ", e);
        }
    }
}
