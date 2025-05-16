package cm.fastrelay.usermanager.controllers;

import cm.fastrelay.usermanager.api.UserApi;
import cm.fastrelay.usermanager.model.RegisterUserDto;
import cm.fastrelay.usermanager.model.UpdateUserDto;
import cm.fastrelay.usermanager.model.UserDto;
import cm.fastrelay.usermanager.model.UserIdDto;
import cm.fastrelay.usermanager.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<Void> deleteUserById(UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrentUserBy(getCurrentUsername()));
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserIdDto> registerUser(RegisterUserDto registerUserDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(registerUserDto));
    }

    @Override
    public ResponseEntity<UserDto> updateMyUserInfo(UpdateUserDto updateUserDto) {
        userService.updateUser(getCurrentUsername(), updateUserDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private String getCurrentUsername() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUsername == null) {
            log.error("The Current Username Is Null");
            throw new RuntimeException("The current username is null");
        }
        return currentUsername;
    }
}
