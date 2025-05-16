package cm.fastrelay.usermanager.domain.users;


import cm.fastrelay.usermanager.domain.converter.PhoneNumberToStringConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberToStringConverterTest {
    private PhoneNumberToStringConverter objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = new PhoneNumberToStringConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        PhoneNumber phoneNumber = PhoneNumber.builder()
            .countryCode("237")
            .number("677777777")
            .build();
        String resultUnderTest = objectUnderTest.convertToDatabaseColumn(phoneNumber);

        assertThat(resultUnderTest).isEqualTo("237;677777777");
    }

    @Test
    void convertToEntityAttribute() {
        PhoneNumber phoneNumber = PhoneNumber.builder()
            .countryCode("237")
            .number("677777777")
            .build();
        PhoneNumber resultUnderTest = objectUnderTest.convertToEntityAttribute("237;677777777");

        assertThat(resultUnderTest).isEqualTo(phoneNumber);
    }
}