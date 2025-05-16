package cm.fastrelay.usermanager.services;

import cm.fastrelay.keycloak.api.KeycloakAdminUserApi;
import cm.fastrelay.keycloak.api.KeycloakAuthApi;
import cm.fastrelay.keycloak.dto.KeycloakTokenResponseDto;
import cm.fastrelay.keycloak.dto.KeycloakUserDto;
import cm.fastrelay.usermanager.config.security.TokenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

/**
 * <p>
 * <strong>DynamicTokenInterceptor</strong>: this class is responsible for
 * dynamically injecting the authentication token into each outgoing request.
 * </p>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakAuthApi keycloakAuthApi;
    private final KeycloakAdminUserApi keycloakAdminUserApi;

    @Value("${keycloak.client.id}")
    private String clientId;

    @Value("${keycloak.client.secret}")
    private String clientSecret;

    @Value("${keycloak.realm.name}")
    private String realm;

    public String getToken() {
        String grantType = "client_credentials";
        ResponseEntity<KeycloakTokenResponseDto> response = keycloakAuthApi.getToken(grantType, clientId, clientSecret);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getAccessToken();
        } else {
            log.error("Failed to retrieve access token from Keycloak");
            throw new IllegalStateException("Failed to retrieve access token from Keycloak");
        }
    }

    public String createUser(KeycloakUserDto keycloakUserDto) {
        try {
            String token = this.getToken();
            TokenContext.setToken(token);

            ResponseEntity<Void> response = keycloakAdminUserApi.createUser(keycloakUserDto);
            HttpHeaders headers = response.getHeaders();
            URI location = headers.getLocation();

            if (location == null || !response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatusCode());
                throw new IllegalStateException("User creation failed in Keycloak");
            }

            String path = location.getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);

            log.info("User successfully created in realm {} with ID {}", realm, userId);
            return userId;
        } finally {
            TokenContext.clear();
        }
    }

    public void deleteByUserId(UUID id) {
        try {
            String token = this.getToken();
            TokenContext.setToken(token);

            ResponseEntity<Void> response = keycloakAdminUserApi.deleteUser(id.toString());

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to delete user in Keycloak. Status: {}", response.getStatusCode());
                throw new IllegalStateException("User deletion failed in Keycloak");
            }

            log.info("User with ID {} successfully deleted from realm {}", id, realm);
        } finally {
            TokenContext.clear();
        }
    }

    public void update(String userId, KeycloakUserDto keycloakUserDto) {
        try {
            String token = this.getToken();
            TokenContext.setToken(token);

            ResponseEntity<Void> response = keycloakAdminUserApi.updateUser(userId, keycloakUserDto);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to update user in Keycloak. Status: {}", response.getStatusCode());
                throw new IllegalStateException("User update failed in Keycloak");
            }

            log.info("User with ID {} successfully updated in realm {}", userId, realm);
        } finally {
            TokenContext.clear();
        }
    }
}
