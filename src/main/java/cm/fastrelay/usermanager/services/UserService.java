package cm.fastrelay.usermanager.services;

import cm.fastrelay.usermanager.domain.users.User;
import cm.fastrelay.usermanager.model.RegisterUserDto;
import cm.fastrelay.usermanager.model.UserIdDto;
import cm.fastrelay.usermanager.repository.UserSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final KeycloakService keycloakService;
    private final UserSpringRepository userSpringRepository;
    private final PasswordEncoder passwordEncoder;

    public UserIdDto createUser(RegisterUserDto registerUserDto) {

        UUID keycloakUserId = keycloakService.createUser(registerUserDto);

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setEmail(registerUserDto.getEmail());
        user.setEnabled(true);
        user.setId(keycloakUserId);

        userSpringRepository.save(user);

        return new UserIdDto().id(keycloakUserId);
    }
}
