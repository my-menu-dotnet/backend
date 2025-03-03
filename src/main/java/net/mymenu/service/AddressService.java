package net.mymenu.service;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import net.mymenu.models.Address;
import net.mymenu.validators.StateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private GoogleMapService googleMapService;

    public Address getAddressByCep(String cep) {
        GeocodingResult[] geocodingResults = googleMapService.geocodeCEP(cep);

        if (geocodingResults == null || geocodingResults.length == 0) {
            return null;
        }

        GeocodingResult result = geocodingResults[0];

        String street = "";
        String neighborhood = "";
        String city = "";
        String state = "";
        String number = "";

        for (AddressComponent component : result.addressComponents) {
            if (containsType(component, AddressComponentType.ROUTE)) {
                street = component.longName;
            }
            if (containsType(component, AddressComponentType.SUBLOCALITY)) {
                neighborhood = component.longName;
            }
            if (containsType(component, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2)) {
                city = component.longName;
            }
            if (containsType(component, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1)) {
                state = component.shortName;
            }
            if (containsType(component, AddressComponentType.STREET_NUMBER)) {
                number = component.longName;
            }
        }

        if (!StateValidator.STATES.contains(state)) {
            state = null;
        }

        return Address.builder()
                .street(street)
                .number(number)
                .neighborhood(neighborhood)
                .city(city)
                .state(state)
                .zipCode(cep)
                .latitude(result.geometry.location.lat)
                .longitude(result.geometry.location.lng)
                .validated(true)
                .build();
    }

    private boolean containsType(AddressComponent component, AddressComponentType type) {
        return component.types != null && java.util.Arrays.asList(component.types).contains(type);
    }

}
