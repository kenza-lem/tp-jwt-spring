package ma.enset.tpjwtspring.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {
        return Map.of("email", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority()).toList());
    }
}
