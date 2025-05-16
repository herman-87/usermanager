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
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CommonMapper.class}
)
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "fromPhoneNumberToPhoneNumberDto")
    User fromRegisterUserDtoToUser(RegisterUserDto registerUserDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "credentials", source = "registerUserDto", qualifiedByName = "fromRegisterUserDtoToKeycloakCredentialDtoList")
    KeycloakUserDto fromRegisterUserDtoToKeycloakUserDTO(RegisterUserDto registerUserDto);

    @Named("fromRegisterUserDtoToKeycloakCredentialDtoList")
    default List<KeycloakCredentialDto> fromRegisterUserDtoToKeycloakCredentialDtoList(RegisterUserDto registerUserDto) {
        return List.of(new KeycloakCredentialDto()
                .type("password")
                .value(registerUserDto.getPassword())
                .temporary(false)
        );
    }

    default UserIdDto fromUuidToUserIdDto(UUID userId) {
        return new UserIdDto().id(userId);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "fromPhoneNumberDtoToPhoneNumber")
    UserDto toDto(User user);


    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "fromPhoneNumberToPhoneNumberDto")
    void update(@MappingTarget User user, UpdateUserDto updateUserDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    KeycloakUserDto fromUpdateUserDtoToKeycloakUserDto(UpdateUserDto updateUserDto);
}
