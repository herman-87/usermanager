package cm.fastrelay.usermanager.mapper;

import cm.fastrelay.usermanager.domain.users.PhoneNumber;
import cm.fastrelay.usermanager.model.PhoneNumberDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CommonMapper {

    @Named("fromPhoneNumberDtoToPhoneNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "countryCode", source = "countryCode")
    @Mapping(target = "number", source = "number")
    PhoneNumberDto fromPhoneNumberDtoToPhoneNumber(PhoneNumber phoneNumber);

    @Named("fromPhoneNumberToPhoneNumberDto")
    @InheritInverseConfiguration(name = "fromPhoneNumberDtoToPhoneNumber")
    PhoneNumber fromPhoneNumberToPhoneNumberDto(PhoneNumberDto phoneNumberDto);
}
