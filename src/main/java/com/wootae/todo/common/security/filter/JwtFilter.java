package com.wootae.todo.common.security.filter;

import com.wootae.todo.common.security.auth.CustomUserDetails;
import com.wootae.todo.common.security.util.JwtUtil;
import com.wootae.todo.domain.user.dto.Role;
import com.wootae.todo.domain.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Cookie[] cookies = request.getCookies();

        // ★ 수정된 부분: cookies가 null이 아닐 때만 반복문을 실행합니다.
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        try{

            if(token == null || jwtUtil.isExpired(token)){
                filterChain.doFilter(request,response);
                return;
            }

            String username = jwtUtil.getUsername(token);
            String password = "TestPassword";
            String role = jwtUtil.getRole(token);

            User user = User.builder()
                    .username(username)
                    .password(password)
                    .role(Role.findByKey(role))
                    .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request,response);

        } catch (
        ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다. 쿠키를 삭제하고 리다이렉트합니다.");

            // ★ [핵심] 만료된 쿠키 삭제 (이게 없으면 무한루프!)
            Cookie cookie = new Cookie("Authorization", null);
            cookie.setMaxAge(0);
            cookie.setPath("/"); // 생성할 때 설정한 path와 똑같아야 삭제됨
            response.addCookie(cookie);

            // 응답으로 리다이렉트 (로그인 페이지로 보냄)
            response.sendRedirect("/user/login");

            // 더 이상 진행하지 않음
            return;
        }

    }
}
