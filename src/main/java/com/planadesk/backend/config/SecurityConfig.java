//package com.planadesk.backend.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
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
//public class SecurityConfig {
//
//    private final JwtFilter jwtFilter;
//
//    public SecurityConfig(JwtFilter jwtFilter) {
//        this.jwtFilter = jwtFilter;
//    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            throw new UnsupportedOperationException("No default users");
//        };
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(csrf -> csrf.disable())
//            .sessionManagement(sm ->
//                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            )
//            .authorizeHttpRequests(auth -> auth
//
//                // Preflight
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                // Public
//                .requestMatchers("/auth/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
//
//                // 🔐 CART (ALL METHODS)
//                .requestMatchers("/api/cart/**").authenticated()
//
//                // Everything else
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // ✅ CORS configuration for cookie-based JWT
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // ❗ DO NOT use "*"
//        config.setAllowedOrigins(List.of(
//            "http://localhost:5173",
//            "http://localhost:8080",
//            "https://planadesk-f.vercel.app"
//        ));
//
//        config.setAllowedMethods(List.of(
//            "GET", "POST", "PUT", "DELETE", "OPTIONS"
//        ));
//
//        config.setAllowedHeaders(List.of("*"));
//
//        //  REQUIRED for cookies
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source =
//            new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}


package com.planadesk.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.planadesk.backend.security.JwtFilter;

@Configuration
@EnableMethodSecurity   // ✅ Enables @PreAuthorize
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ---------------- CORS ----------------
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ---------------- CSRF ----------------
            // ❗ Safe because JWT cookie MUST be SameSite=Strict
            .csrf(csrf -> csrf.disable())

            // ---------------- STATELESS ----------------
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ---------------- AUTH RULES ----------------
            .authorizeHttpRequests(auth -> auth

            	    // 🔓 AUTH ENDPOINTS
            		.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
            		.requestMatchers(HttpMethod.GET, "/auth/**").permitAll()

            	    // Preflight
            	    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            	    // ---------------- PUBLIC READ ----------------
            	    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
            	    .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
            	    .requestMatchers(HttpMethod.GET, "/api/sections/**").permitAll()

            	    // ---------------- USER ----------------
            	    .requestMatchers(HttpMethod.GET, "/api/orders/my").authenticated()
            	    .requestMatchers("/api/cart/**").authenticated()

            	    // ---------------- ADMIN ----------------
            	    .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")

            	    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

            	    .requestMatchers(HttpMethod.POST, "/api/countries/**").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.PUT, "/api/countries/**").hasRole("ADMIN")
            	    .requestMatchers(HttpMethod.DELETE, "/api/countries/**").hasRole("ADMIN")

            	    .requestMatchers("/api/admin/**").hasRole("ADMIN")

            	    // ---------------- EVERYTHING ELSE ----------------
            	    .anyRequest().authenticated()
            	)

            // ---------------- JWT FILTER ----------------
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ---------------- PASSWORD ENCODER ----------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // stronger cost
    }

    // ---------------- CORS CONFIG ----------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "http://localhost:8080", // 🔥 FRONTEND
            
            
            "https://planadesk-f.vercel.app"
        ));

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization",
            "X-Requested-With"
        ));

        // 🔥 REQUIRED for cookies
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

