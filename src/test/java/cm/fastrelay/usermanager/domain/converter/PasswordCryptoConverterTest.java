package cm.fastrelay.usermanager.domain.converter;


import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PasswordCryptoConverterTest {

    private final PasswordCryptoConverter converter = new PasswordCryptoConverter();

    @Test
    void shouldEncodeRawPassword() {
        String rawPassword = "mySecret123";

        String encodedPassword = converter.convertToDatabaseColumn(rawPassword);
        System.out.println("\n ||||||||||||||||||||" + rawPassword + " -> " + encodedPassword + "|||||||||||||||||||||\n");

        assertThat(encodedPassword).isNotBlank();
        assertThat(encodedPassword).startsWith("$2a$");

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void shouldReturnAlreadyEncodedPasswordAsIs() {
        String alreadyEncoded = new BCryptPasswordEncoder().encode("existing");

        String result = converter.convertToDatabaseColumn(alreadyEncoded);

        assertThat(alreadyEncoded).isEqualTo(result);
    }

    @Test
    void shouldThrowExceptionForNullPassword() {
        assertThrows(IllegalArgumentException.class, () ->
            converter.convertToDatabaseColumn(null));
    }

    @Test
    void shouldThrowExceptionForBlankPassword() {
        assertThrows(IllegalArgumentException.class, () ->
            converter.convertToDatabaseColumn("  "));
    }

    @Test
    void shouldReturnEncodedPasswordAsIsOnRead() {
        String encoded = new BCryptPasswordEncoder().encode("abc123");

        String result = converter.convertToEntityAttribute(encoded);

        assertThat(encoded).isEqualTo(result);
    }
}
