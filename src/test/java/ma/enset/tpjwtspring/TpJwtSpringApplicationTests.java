package ma.enset.tpjwtspring;

import ma.enset.tpjwtspring.entity.AppUser;
import ma.enset.tpjwtspring.repository.UserRepository;
import ma.enset.tpjwtspring.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@org.springframework.test.context.TestExecutionListeners(listeners = {org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class, org.springframework.test.context.transaction.TransactionalTestExecutionListener.class}, mergeMode = org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
@SpringBootTest
@Transactional
class TpJwtSpringApplicationTests extends SecurityTestSupport {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    void contextLoads() {
    }

    @Test
    void savedUserCanBeLoadedByEmailWithItsRole() {
        AppUser saved = userRepository.saveAndFlush(
                new AppUser(null, "encoded-password", "test@example.com", "USER"));
        assertNotNull(saved.getId());
        var details = userDetailsService.loadUserByUsername("test@example.com");
        assertEquals("test@example.com", details.getUsername());
        assertEquals("encoded-password", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void unknownEmailIsRejected() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("absent@example.com"));
    }
}
