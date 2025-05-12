package cm.fastrelay.usermanager.services;

import cm.fastrelay.usermanager.model.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KeycloakService {

    private final RestClient keycloakRestClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Transactional
    public UUID createUser(RegisterUserDto userDto) {
        String token = getAdminToken();

        var userPayload = Map.of(
                "username", userDto.getUsername(),
                "email", userDto.getEmail(),
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", userDto.getPassword(),
                        "temporary", false
                ))
        );

        URI location = keycloakRestClient.post()
                .uri("/admin/realms/{realm}/users", realm)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(userPayload)
                .retrieve()
                .toBodilessEntity()
                .getHeaders()
                .getLocation();

        return extractUserId(location);
    }

    private String getAdminToken() {
        var formData = Map.of(
                "client_id", clientId,
                "username", adminUsername,
                "password", adminPassword,
                "grant_type", "password"
        );

        var response = keycloakRestClient.post()
                .uri("/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toEntity(Map.class)
                .getBody();

        return (String) response.get("access_token");
    }

    private UUID extractUserId(URI location) {
        String path = location.getPath();
        return UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
    }
}
