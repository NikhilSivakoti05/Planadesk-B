//package com.planadesk.backend.security;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return request.getRequestURI().startsWith("/auth/");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req,
//                                    HttpServletResponse res,
//                                    FilterChain chain)
//            throws ServletException, IOException {
//
//        String token = null;
//
//        // 🔥 Cookie-based JWT (PRIMARY)
//        if (req.getCookies() != null) {
//            for (Cookie c : req.getCookies()) {
//                if ("jwt".equals(c.getName())) {
//                    token = c.getValue();
//                    break;
//                }
//            }
//        }
//
//        
//
//        if (token != null && jwtUtil.valid(token)) {
//            String email = jwtUtil.getEmail(token);
//            String role = jwtUtil.getRole(token);
//
//            UsernamePasswordAuthenticationToken auth =
//                new UsernamePasswordAuthenticationToken(
//                    email,
//                    null,
//                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
//                );
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(req, res);
//    }
//}
package com.planadesk.backend.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Skip auth endpoints & preflight
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth/")
            || HttpMethod.OPTIONS.matches(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        // 🔒 Always start clean
        SecurityContextHolder.clearContext();

        String token = extractJwtFromCookie(request);

        if (token != null) {
            try {
                if (jwtUtil.valid(token)) {

                    String email = jwtUtil.getEmail(token);
                    String role = jwtUtil.getRole(token);

                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + role)
                            )
                        );

                    authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                            .buildDetails(request)
                    );

                    SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
                }
            } catch (Exception ex) {
                // ❗ Invalid token → ensure no auth remains
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    // ---------------- HELPER ----------------
    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
