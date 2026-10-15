package ma.enset.tpjwtspring.controller;

import ma.enset.tpjwtspring.dto.AuthResponse;
import ma.enset.tpjwtspring.dto.LoginRequest;
import ma.enset.tpjwtspring.dto.RegisterRequest;
import ma.enset.tpjwtspring.entity.AppUser;
import ma.enset.tpjwtspring.repository.UserRepository;
import ma.enset.tpjwtspring.service.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        validateCredentials(request.email(), request.password());
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email deja utilise");
        }
        AppUser user = new AppUser(null, passwordEncoder.encode(request.password()), request.email(), "USER");
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.badRequest().body("Email deja utilise");
        }
        return ResponseEntity.ok("Utilisateur cree");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        validateCredentials(request.email(), request.password());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.email(), request.password()));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants incorrects");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    private void validateCredentials(String email, String password) {
        if (email == null || email.isBlank() || email.length() > 255
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || password == null || password.isBlank()
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email et mot de passe invalides");
        }
    }
}
