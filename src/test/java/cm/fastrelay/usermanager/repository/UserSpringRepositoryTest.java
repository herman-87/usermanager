package cm.fastrelay.usermanager.repository;

import cm.fastrelay.usermanager.domain.UuidBaseEntity;
import cm.fastrelay.usermanager.domain.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@DataJpaTest
class UserSpringRepositoryTest {
    @Autowired
    private UserSpringRepository userSpringRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void testFindUserByUsername() {
        String username = "username-test";
        String expectedUserId = jdbcClient.sql("SELECT c_id FROM t_user WHERE c_username = :username")
            .param("username", username)
            .query(String.class)
            .single();

        Optional<User> resultUnderTest = userSpringRepository.findUserByUsername(username);

        assertThat(resultUnderTest)
            .map(UuidBaseEntity::getId)
            .map(UUID::toString)
            .contains(expectedUserId);
    }
}