//package com.planadesk.backend.auth;
//
//import java.util.Map;
//
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService service;
//    public AuthController(AuthService s){ service=s; }
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequest r){
//        service.signup(r);
//        return ResponseEntity.ok("Signup successful");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest r,
//                                   HttpServletResponse response) {
//
//        AuthResponse auth = service.login(r);
//
//        ResponseCookie cookie = ResponseCookie.from("jwt", auth.getToken())
//                .httpOnly(true)
//                .secure(true) // true in HTTPS
//                .sameSite("None") //  REQUIRED
//                .path("/")
//                .maxAge(24 * 60 * 60)
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//
//        return ResponseEntity.ok(Map.of(
//            "role", auth.getRole(),
//            "firstName", auth.getFirstName(),
//            "lastName", auth.getLastName()
//        ));
//    }
//
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//
//        ResponseCookie cookie = ResponseCookie.from("jwt", "")
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("None")
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//        return ResponseEntity.ok("Logged out");
//    }
//
////    @PostMapping("/create-admin-temp")
////    public String createTempAdmin() {
////        service.createTempAdmin();
////        return "Temporary admin created";
////    }
//
//}
//package com.planadesk.backend.auth;
//
//import java.time.Duration;
//import java.util.Map;
//
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.planadesk.backend.dto.ForgotPasswordRequest;
//import com.planadesk.backend.dto.ResetPasswordRequest;
//import com.planadesk.backend.service.PasswordResetService;
//import jakarta.validation.Valid;
//
//import jakarta.servlet.http.HttpServletResponse;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService service;
//    private final PasswordResetService passwordResetService;
//
//    public AuthController(
//            AuthService service,
//            PasswordResetService passwordResetService) {
//
//        this.service = service;
//        this.passwordResetService = passwordResetService;
//    }
//
//    // ---------------- SIGNUP ----------------
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
//        service.signup(request);
//        return ResponseEntity.ok("Signup successful");
//    }
//
//    // ---------------- LOGIN ----------------
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestBody LoginRequest request,
//            HttpServletResponse response) {
//
//        AuthResponse auth = service.login(request);
//
//        // 🔥 SECURE JWT COOKIE
//        ResponseCookie cookie = ResponseCookie.from("jwt", auth.getToken())
//                .httpOnly(true)
//                .secure(true)               // HTTPS only
//                .sameSite("Strict")         // 🔥 CSRF protection
//                .path("/")
//                .maxAge(Duration.ofHours(2)) // safer lifetime
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//
//        // ❗ Never send token in response body
//        return ResponseEntity.ok(Map.of(
//                "role", auth.getRole(),
//                "firstName", auth.getFirstName(),
//                "lastName", auth.getLastName()
//        ));
//    }
//
//    // ---------------- LOGOUT ----------------
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//
//        ResponseCookie cookie = ResponseCookie.from("jwt", "")
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("Strict")
//                .path("/")
//                .maxAge(0)
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//        return ResponseEntity.ok("Logged out");
//    }
//
//    // ---------------- FORGOT PASSWORD ----------------
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> forgotPassword(
//    		@Valid @RequestBody ForgotPasswordRequest request) {
//
//        passwordResetService
//                .requestPasswordReset(request.getEmail());
//
//        // ❗ Always generic (prevents email enumeration)
//        return ResponseEntity.ok(
//                "If the email exists, a password reset link has been sent"
//        );
//    }
//
//    // ---------------- RESET PASSWORD ----------------
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(
//    		@Valid @RequestBody ResetPasswordRequest request) {
//
//        passwordResetService.resetPassword(
//                request.getToken(),
//                request.getNewPassword()
//        );
//
//        return ResponseEntity.ok("Password reset successful");
//    }
//}
//

//package com.planadesk.backend.auth;
//
//import java.time.Duration;
//import java.util.Map;
//
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.planadesk.backend.dto.ForgotPasswordRequest;
//import com.planadesk.backend.dto.ResetPasswordRequest;
//import com.planadesk.backend.service.PasswordResetService;
//
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService service;
//    private final PasswordResetService passwordResetService;
//
//    public AuthController(
//            AuthService service,
//            PasswordResetService passwordResetService) {
//
//        this.service = service;
//        this.passwordResetService = passwordResetService;
//    }
//
//    /* =======================
//       SIGNUP
//       ======================= */
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
//        service.signup(request);
//        return ResponseEntity.ok("Signup successful");
//    }
//
//    /* =======================
//       LOGIN
//       ======================= */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestBody LoginRequest request,
//            HttpServletResponse response) {
//
//        AuthResponse auth = service.login(request);
//
//        // 🔐 Cross-site production cookie (Vercel + Render)
//        ResponseCookie cookie = ResponseCookie.from("jwt", auth.getToken())
//        	    .httpOnly(true)
//        	    .secure(true)
//        	    .sameSite("None")
//        	    
//        	    .path("/")
//        	    .maxAge(Duration.ofHours(2))
//        	    .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//
//        return ResponseEntity.ok(Map.of(
//                "role", auth.getRole(),
//                "firstName", auth.getFirstName(),
//                "lastName", auth.getLastName()
//        ));
//    }
//
//    /* =======================
//       LOGOUT
//       ======================= */
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//
//        // ⚠ Must match login cookie settings exactly
//    	ResponseCookie cookie = ResponseCookie.from("jwt", "")
//    		    .httpOnly(true)
//    		    .secure(true)
//    		    .sameSite("None")
//    		    
//    		    .path("/")
//    		    .maxAge(0)
//    		    .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//
//        return ResponseEntity.ok("Logged out");
//    }
//
//    /* =======================
//       FORGOT PASSWORD
//       ======================= */
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> forgotPassword(
//            @Valid @RequestBody ForgotPasswordRequest request) {
//
//        passwordResetService
//                .requestPasswordReset(request.getEmail());
//
//        return ResponseEntity.ok(
//                "If the email exists, a password reset link has been sent"
//        );
//    }
//
//    /* =======================
//       RESET PASSWORD
//       ======================= */
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(
//            @Valid @RequestBody ResetPasswordRequest request) {
//
//        passwordResetService.resetPassword(
//                request.getToken(),
//                request.getNewPassword()
//        );
//
//        return ResponseEntity.ok("Password reset successful");
//    }
//}
//package com.planadesk.backend.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import com.planadesk.backend.security.JwtFilter;
//
//@Configuration
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//            // 🔥 IMPORTANT: enable CORS at SECURITY level
//            .cors(Customizer.withDefaults())
//
//            .csrf(csrf -> csrf.disable())
//
//            .sessionManagement(sm ->
//                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .requestMatchers("/auth/**").permitAll()
//
//                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/sections/**").permitAll()
//
//                .requestMatchers("/api/cart/**").authenticated()
//                .requestMatchers(HttpMethod.GET, "/api/orders/my").authenticated()
//
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//
//                .anyRequest().authenticated()
//            )
//
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
//
//    // 🔥 CORS CONFIG (THIS FIXES COOKIE ISSUE)
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(List.of(
//            "http://localhost:5173",
//            "https://planadesk-f.vercel.app"
//        ));
//
//        config.setAllowedMethods(List.of(
//            "GET", "POST", "PUT", "DELETE", "OPTIONS"
//        ));
//
//        config.setAllowedHeaders(List.of("*"));
//
//        // 🔥 REQUIRED for cookies
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//            new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}
package com.planadesk.backend.auth;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.planadesk.backend.dto.ForgotPasswordRequest;
import com.planadesk.backend.dto.ResetPasswordRequest;
import com.planadesk.backend.service.PasswordResetService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService service,
                          PasswordResetService passwordResetService) {
        this.service = service;
        this.passwordResetService = passwordResetService;
    }

    // ================= SIGNUP =================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        AuthResponse auth = service.login(request);

        // ⚠ IMPORTANT: secure(false) for localhost HTTP
     // In AuthController.java -> login() method
        ResponseCookie cookie = ResponseCookie.from("jwt", auth.getToken())
                .httpOnly(true)
                .secure(true)               // 🔥 MUST be true for production (HTTPS)
                .sameSite("None")           // 🔥 MUST be "None" to allow Vercel to talk to Render
                .path("/")
                .maxAge(Duration.ofHours(2))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of(
                "role", auth.getRole(),
                "firstName", auth.getFirstName(),
                "lastName", auth.getLastName()
        ));
    }

    // ================= LOGOUT =================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)     // must match login cookie
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordResetService.requestPasswordReset(request.getEmail());

        return ResponseEntity.ok(
                Map.of("message",
                        "If the email exists, a password reset link has been sent")
        );
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                Map.of("message", "Password reset successful")
        );
    }
}