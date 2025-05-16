package cm.fastrelay.usermanager.repository;

import cm.fastrelay.usermanager.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSpringRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String currentUsername);
}
