package com.alumni.career.controller;

import com.alumni.career.dto.LoginRequest;
import com.alumni.career.dto.RegisterRequest;
import com.alumni.career.model.Alumni;
import com.alumni.career.model.User;
import com.alumni.career.repository.AlumniRepository;
import com.alumni.career.repository.UserRepository;
import com.alumni.career.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlumniRepository alumniRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email already registered"));
            }

            // Create user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(User.Role.ALUMNI);
            user.setIsActive(true);

            User savedUser = userRepository.save(user);

            // Create alumni profile
            Alumni alumni = new Alumni();
            alumni.setUser(savedUser);
            alumni.setFirstName(request.getFirstName());
            alumni.setLastName(request.getLastName());
            alumni.setPhone(request.getPhone());
            alumni.setGraduationYear(request.getGraduationYear());
            alumni.setDegree(request.getDegree());
            alumni.setMajor(request.getMajor());

            alumniRepository.save(alumni);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("userId", savedUser.getUserId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
            }

            if (!user.getIsActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account is inactive"));
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getUserId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().toString());
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid token"));
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, email)) {
                return ResponseEntity.ok(Map.of("valid", true, "email", email));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", e.getMessage()));
        }
    }
}
