package ma.enset.tpjwtspring;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.security.SecureRandom;
import java.util.Base64;

abstract class SecurityTestSupport {
    private static final String SECRET = newSecret();

    private static String newSecret() {
        byte[] bytes = new byte[48];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @DynamicPropertySource
    static void configureTestSecret(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> SECRET);
    }
}
