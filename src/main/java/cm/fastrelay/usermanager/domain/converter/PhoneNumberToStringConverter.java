package cm.fastrelay.usermanager.domain.converter;

import cm.fastrelay.usermanager.domain.users.PhoneNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PhoneNumberToStringConverter implements AttributeConverter<PhoneNumber, String> {
    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.getCountryCode().concat(";").concat(phoneNumber.getNumber());
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        String[] phoneNumberData = phoneNumber.split(";");
        return PhoneNumber.builder().countryCode(phoneNumberData[0]).number(phoneNumberData[1]).build();
    }
}
