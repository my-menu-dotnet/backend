package net.mymenu.mapper;

import net.mymenu.dto.address.AddressResponse;
import net.mymenu.models.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toAddressResponse(Address address);
}
