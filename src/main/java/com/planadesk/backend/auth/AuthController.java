package com.planadesk.backend.auth;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    public AuthController(AuthService s){ service=s; }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest r){
        service.signup(r);
        return ResponseEntity.ok("Signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r,
                                   HttpServletResponse response) {

        AuthResponse auth = service.login(r);

        ResponseCookie cookie = ResponseCookie.from("jwt", auth.getToken())
                .httpOnly(true)
                .secure(true) // true in HTTPS
                .sameSite("None") //  REQUIRED
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(Map.of(
            "role", auth.getRole(),
            "firstName", auth.getFirstName(),
            "lastName", auth.getLastName()
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok("Logged out");
    }

//    @PostMapping("/create-admin-temp")
//    public String createTempAdmin() {
//        service.createTempAdmin();
//        return "Temporary admin created";
//    }

}
