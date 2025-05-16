package cm.fastrelay.usermanager.domain.users;

import cm.fastrelay.usermanager.domain.converter.PasswordCryptoConverter;
import cm.fastrelay.usermanager.domain.UuidBaseEntity;
import cm.fastrelay.usermanager.domain.converter.PhoneNumberToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuperBuilder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User extends UuidBaseEntity implements UserDetails {

    @Column(name = "c_username")
    private String username;

    @Convert(converter = PasswordCryptoConverter.class)
    @Column(name = "c_password")
    private String password;

    @Column(name = "c_email")
    private String email;

    @Builder.Default
    @Column(name = "c_enabled")
    private boolean enabled = true;

    @Column(name = "c_firstname")
    private String firstName;

    @Column(name = "c_lastname")
    private String lastName;

    @Builder.Default
    @Column(name = "c_email_is_verified")
    private boolean emailVerified = false;

    @Column(name = "c_phone_number")
    @Convert(converter = PhoneNumberToStringConverter.class)
    private PhoneNumber phoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
