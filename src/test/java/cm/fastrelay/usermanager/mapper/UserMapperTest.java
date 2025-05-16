package cm.fastrelay.usermanager.mapper;


import cm.fastrelay.keycloak.dto.KeycloakCredentialDto;
import cm.fastrelay.keycloak.dto.KeycloakUserDto;
import cm.fastrelay.usermanager.domain.users.PhoneNumber;
import cm.fastrelay.usermanager.domain.users.User;
import cm.fastrelay.usermanager.model.PhoneNumberDto;
import cm.fastrelay.usermanager.model.RegisterUserDto;
import cm.fastrelay.usermanager.model.UpdateUserDto;
import cm.fastrelay.usermanager.model.UserDto;
import cm.fastrelay.usermanager.model.UserIdDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper objectUnderTest = new UserMapperImpl(new CommonMapperImpl());

    @Test
    void testFromRegisterUserDtoToUser() {
        // Given
        String username = "john";
        String email = "john@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password";
        String countryCode = "237";
        String number = "677777777";
        RegisterUserDto registerUserDto = new RegisterUserDto()
            .username(username)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .password(password)
            .phoneNumber(new PhoneNumberDto().countryCode(countryCode).number(number));

        // When
        User resultUnderTest = objectUnderTest.fromRegisterUserDtoToUser(registerUserDto);

        // Then
        assertThat(resultUnderTest)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(
                User.builder()
                    .username(username)
                    .lastName(lastName)
                    .firstName(firstName)
                    .email(email)
                    .password(password)
                    .phoneNumber(new PhoneNumber(countryCode, number))
                    .emailVerified(false)
                    .enabled(true)
                    .build()
            );
    }

    @Test
    void testFromRegisterUserDtoToKeycloakUserDTO() {
        // Given
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("alice");
        dto.setEmail("alice@example.com");
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setPassword("mypassword");

        // When
        KeycloakUserDto keycloakUserDTO = objectUnderTest.fromRegisterUserDtoToKeycloakUserDTO(dto);

        // Then
        assertThat(keycloakUserDTO).isNotNull();
        assertThat(keycloakUserDTO.getUsername()).isEqualTo("alice");
        assertThat(keycloakUserDTO.getEmail()).isEqualTo("alice@example.com");
        assertThat(keycloakUserDTO.getFirstName()).isEqualTo("Alice");
        assertThat(keycloakUserDTO.getLastName()).isEqualTo("Smith");
        assertThat(keycloakUserDTO.getEnabled()).isTrue();
        assertThat(keycloakUserDTO.getEmailVerified()).isFalse();

        List<KeycloakCredentialDto> credentials = keycloakUserDTO.getCredentials();
        assertThat(credentials).hasSize(1);
        assertThat(credentials.getFirst().getType()).isEqualTo("password");
        assertThat(credentials.getFirst().getValue()).isEqualTo("mypassword");
        assertThat(credentials.getFirst().getTemporary()).isFalse();
    }

    @Test
    void testFromUuidToUserIdDto() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        UserIdDto userIdDto = objectUnderTest.fromUuidToUserIdDto(userId);

        // Then
        assertThat(userIdDto).isNotNull();
        assertThat(userIdDto.getId()).isEqualTo(userId);
    }

    @Test
    void toDtoTest() {
        // Given
        UUID id = UUID.randomUUID();
        String username = "johndoe";
        String email = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String countryCode = "237";
        String number = "677777777";
        User user = User.builder()
            .id(id)
            .username(username)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .emailVerified(true)
            .enabled(true)
            .phoneNumber(
                PhoneNumber.builder()
                    .countryCode(countryCode)
                    .number(number)
                    .build()
            )
            .build();

        // When
        UserDto resultUnderTest = objectUnderTest.toDto(user);

        // Then
        assertThat(resultUnderTest)
            .usingRecursiveComparison()
            .isEqualTo(
                new UserDto()
                    .id(id)
                    .username(username)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(
                        new PhoneNumberDto()
                            .countryCode(countryCode)
                            .number(number)
                    )
            );
    }

    @Test
    void testUpdate() {
        String newUsername = "john";
        String newEmail = "john@example.com";
        String newFirstName = "John";
        String newLastName = "Doe";
        String newCountryCode = "237";
        String newNumber = "677777777";
        UpdateUserDto updateUserDto = new UpdateUserDto()
            .username(newUsername)
            .email(newEmail)
            .firstName(newFirstName)
            .lastName(newLastName)
            .phoneNumber(new PhoneNumberDto().countryCode(newCountryCode).number(newNumber));
        String unchanged = "any password";
        User user = User.builder()
            .password(unchanged)
            .emailVerified(false)
            .build();

        objectUnderTest.update(user, updateUserDto);

        assertThat(user)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(
                User.builder()
                    .username(newUsername)
                    .lastName(newLastName)
                    .firstName(newFirstName)
                    .email(newEmail)
                    .password(unchanged)
                    .phoneNumber(new PhoneNumber(newCountryCode, newNumber))
                    .emailVerified(false)
                    .build()
            );
    }

    @Test
    void testFromUpdateUserDtoToKeycloakUserDto() {
        String newUsername = "john";
        String newEmail = "john@example.com";
        String newFirstName = "John";
        String newLastName = "Doe";
        String newCountryCode = "237";
        String newNumber = "677777777";
        UpdateUserDto updateUserDto = new UpdateUserDto()
            .username(newUsername)
            .email(newEmail)
            .firstName(newFirstName)
            .lastName(newLastName)
            .phoneNumber(new PhoneNumberDto().countryCode(newCountryCode).number(newNumber));

        KeycloakUserDto resultUnderTest = objectUnderTest.fromUpdateUserDtoToKeycloakUserDto(updateUserDto);

        assertThat(resultUnderTest)
            .usingRecursiveComparison()
            .isEqualTo(
                new KeycloakUserDto()
                    .username(newUsername)
                    .email(newEmail)
                    .firstName(newFirstName)
                    .lastName(newLastName)
            );
    }
}
