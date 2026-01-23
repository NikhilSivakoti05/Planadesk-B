package com.planadesk.backend.auth;

import com.planadesk.backend.model.*;
import com.planadesk.backend.repository.UserRepository;
import com.planadesk.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder enc;
    private final JwtUtil jwt;

    public AuthService(UserRepository r, PasswordEncoder e, JwtUtil j){
        repo = r;
        enc = e;
        jwt = j;
    }

    // USER SIGNUP
    public void signup(SignupRequest r){
        String email = r.getEmail().toLowerCase().trim();

        if(repo.existsByEmail(email))
            throw new RuntimeException("Email already exists");

        if(!Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
                .matcher(r.getPassword()).matches())
            throw new RuntimeException("Password rule failed");

        User u = new User();
        u.setFirstName(r.getFirstName());
        u.setLastName(r.getLastName());
        u.setEmail(email);
        u.setPassword(enc.encode(r.getPassword()));
        u.setRole(Role.USER);
        u.setEnabled(true);
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        repo.save(u);
    }

    // LOGIN
    public AuthResponse login(LoginRequest r){
        String email = r.getEmail().toLowerCase().trim();
//        System.out.println("LOGIN TRY EMAIL: " + email);
//        System.out.println("INPUT PASSWORD: " + r.getPassword());

        User u = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        

        boolean match = enc.matches(r.getPassword(), u.getPassword());
        
      

//        System.out.println("PASSWORD MATCH: " + match);

        if(!u.isEnabled())
            throw new RuntimeException("Account is blocked");

        if(!match)
            throw new RuntimeException("Wrong password");

        String token = jwt.generateToken(u.getEmail(), u.getRole().name());

        return new AuthResponse(
            token,
            u.getRole().name(),
            u.getFirstName(),
            u.getLastName()
        );
    }

//    // TEMP ADMIN CREATION (DEV ONLY)
//    public void createTempAdmin() {
//        String email = "admin@planadesk.com";
//
//        if (repo.existsByEmail(email)) {
//            System.out.println("Admin already exists");
//            return;
//        }
//
//        User u = new User();
//        u.setFirstName("Admin");
//        u.setLastName("User");
//        u.setEmail(email);
//        u.setPassword(enc.encode("Admin@1999"));
//        u.setRole(Role.ADMIN);
//        u.setEnabled(true);
//        u.setCreatedAt(LocalDateTime.now());
//        u.setUpdatedAt(LocalDateTime.now());
//
//        repo.save(u);
//        System.out.println("Temporary admin created: admin@planadesk.com / Admin@123");
//    }
}
