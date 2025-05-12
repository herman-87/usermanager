package cm.fastrelay.usermanager.domain.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "t_user")
public class User implements UserDetails {

    @Id
    @Column(name = "c_id")
    private UUID id;

    @Column(name = "c_username")
    private String username;

    @Column(name = "c_password")
    private String password;

    @Column(name = "c_email")
    private String email;

    @Column(name = "c_enabled")
    private boolean enabled;

    @Column(name = "c_firstname")
    private String firstName;

    @Column(name = "c_lastname")
    private String lastName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
