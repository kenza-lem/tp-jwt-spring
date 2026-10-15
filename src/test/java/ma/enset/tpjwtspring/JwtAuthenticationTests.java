package ma.enset.tpjwtspring;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ma.enset.tpjwtspring.dto.AuthResponse;
import ma.enset.tpjwtspring.entity.AppUser;
import ma.enset.tpjwtspring.repository.UserRepository;
import ma.enset.tpjwtspring.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(listeners = DependencyInjectionTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
class JwtAuthenticationTests extends SecurityTestSupport {
    @Value("${local.server.port}")
    private int port;
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    private HttpResponse<String> request(String method, String path, String body, String token)
            throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .timeout(Duration.ofSeconds(15));
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        if (body == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(body));
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void registerLoginAndProtectedProfileWorkWithoutSession() throws Exception {
        String email = "parcours@example.com";
        String body = "{\"email\":\"" + email + "\",\"password\":\"123456\"}";
        assertEquals(200, request("POST", "/auth/register", body, null).statusCode());
        AppUser saved = userRepository.findByEmail(email).orElseThrow();
        assertNotEquals("123456", saved.getPassword());
        assertTrue(passwordEncoder.matches("123456", saved.getPassword()));
        assertEquals("USER", saved.getRole());

        var login = request("POST", "/auth/login", body, null);
        assertEquals(200, login.statusCode(), login.body());
        String token = JsonMapper.builder().build().readValue(login.body(), AuthResponse.class).token();
        assertEquals(email, jwtService.extractUsername(token));
        var profile = request("GET", "/api/profile", null, token);
        assertEquals(200, profile.statusCode(), profile.body());
        assertTrue(profile.body().contains(email));
        assertTrue(profile.body().contains("ROLE_USER"));
        assertTrue(profile.headers().allValues("Set-Cookie").stream()
                .noneMatch(cookie -> cookie.contains("JSESSIONID")));
        assertEquals(409, request("POST", "/auth/register", body, null).statusCode());
        assertEquals(401, request("POST", "/auth/login",
                "{\"email\":\"" + email + "\",\"password\":\"wrong\"}", null).statusCode());
        assertEquals(401, request("GET", "/api/profile", null, null).statusCode());
    }

    @Test
    void invalidExpiredAndForgedTokensAreRejected() throws Exception {
        String email = "tokens@example.com";
        userRepository.saveAndFlush(new AppUser(null, passwordEncoder.encode("123456"), email, "USER"));
        var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String expired = Jwts.builder().subject(email)
                .issuedAt(new Date(System.currentTimeMillis() - 120000))
                .expiration(new Date(System.currentTimeMillis() - 60000)).signWith(key).compact();
        String forged = Jwts.builder().subject(email)
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Jwts.SIG.HS384.key().build()).compact();
        String withoutExpiration = Jwts.builder().subject(email).signWith(key).compact();
        for (String token : new String[]{"invalid.jwt", expired, forged, withoutExpiration}) {
            assertEquals(401, request("GET", "/api/profile", null, token).statusCode());
        }
    }

    @Test
    void validTokenWithInsufficientRoleGetsForbidden() throws Exception {
        String email = "guest@example.com";
        userRepository.saveAndFlush(new AppUser(null, passwordEncoder.encode("123456"), email, "GUEST"));
        String token = jwtService.generateToken(User.withUsername(email).password("unused").roles("GUEST").build());
        assertEquals(403, request("GET", "/api/profile", null, token).statusCode());
    }

    @Test
    void tokenForUnknownUserAndWrongSubjectAreRejected() throws Exception {
        var unknown = User.withUsername("unknown@example.com").password("unused").roles("USER").build();
        String token = jwtService.generateToken(unknown);
        assertEquals(401, request("GET", "/api/profile", null, token).statusCode());
        var other = User.withUsername("other@example.com").password("unused").roles("USER").build();
        assertFalse(jwtService.isTokenValid(token, other));
    }

    @Test
    void incompleteCredentialsReturnBadRequest() throws Exception {
        assertEquals(400, request("POST", "/auth/register", "{}", null).statusCode());
        assertEquals(400, request("POST", "/auth/register",
                "{\"email\":\"invalide\",\"password\":\"123456\"}", null).statusCode());
        assertEquals(400, request("POST", "/auth/login", "{}", null).statusCode());
    }
}
