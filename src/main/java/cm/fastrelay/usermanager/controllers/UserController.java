package cm.fastrelay.usermanager.controllers;

import cm.fastrelay.usermanager.api.UserApi;
import cm.fastrelay.usermanager.model.RegisterUserDto;
import cm.fastrelay.usermanager.model.UserIdDto;
import cm.fastrelay.usermanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserIdDto> registerUser(RegisterUserDto registerUserDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(registerUserDto));
    }
}
