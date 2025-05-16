package cm.fastrelay.usermanager.services;

import cm.fastrelay.usermanager.domain.users.User;
import cm.fastrelay.usermanager.exception.ResourceNotFoundException;
import cm.fastrelay.usermanager.mapper.UserMapper;
import cm.fastrelay.usermanager.model.RegisterUserDto;
import cm.fastrelay.usermanager.model.UpdateUserDto;
import cm.fastrelay.usermanager.model.UserDto;
import cm.fastrelay.usermanager.model.UserIdDto;
import cm.fastrelay.usermanager.repository.UserSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final KeycloakService keycloakService;
    private final UserSpringRepository userSpringRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserIdDto createUser(RegisterUserDto registerUserDto) {
        
        String keycloakUserId = keycloakService.createUser(userMapper.fromRegisterUserDtoToKeycloakUserDTO(registerUserDto));
        UUID userId = UUID.fromString(keycloakUserId);

        User user = userMapper.fromRegisterUserDtoToUser(registerUserDto);
        user.setId(userId);

        userSpringRepository.save(user);

        return userMapper.fromUuidToUserIdDto(userId);
    }

    @Transactional
    public UserDto getUserById(UUID id) {
        return userSpringRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public UserDto getCurrentUserBy(String currentUsername) {
        return userSpringRepository.findUserByUsername(currentUsername)
            .map(userMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Transactional
    public void deleteUserById(UUID id) {
        keycloakService.deleteByUserId(id);
        userSpringRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(String currentUser, UpdateUserDto updateUserDto) {
        User user = userSpringRepository.findUserByUsername(currentUser)
            .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        keycloakService.update(user.getId().toString(), userMapper.fromUpdateUserDtoToKeycloakUserDto(updateUserDto));
        userMapper.update(user, updateUserDto);
        userSpringRepository.save(user);
    }
}
