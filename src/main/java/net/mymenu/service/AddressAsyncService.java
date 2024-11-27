package net.mymenu.service;

import net.mymenu.models.Address;
import net.mymenu.repository.AddressRepository;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AddressAsyncService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GoogleMapService googleMapService;

    @Async
    public void addLocation(String cep, Address addressToSave) {
        Address addressWithSameCep = addressRepository.findCoordsByZipCode(cep)
                .orElse(new Address());

        if (addressWithSameCep.getLatitude() != null && addressWithSameCep.getLongitude() != null) {
            addressToSave.setLatitude(addressWithSameCep.getLatitude());
            addressToSave.setLongitude(addressWithSameCep.getLongitude());

            addressRepository.save(addressToSave);

            return;
        }

        GeocodingResult[] geocodingResults = googleMapService.geocodeCEP(cep);
        addressToSave.setLatitude(geocodingResults[0].geometry.location.lat);
        addressToSave.setLongitude(geocodingResults[0].geometry.location.lng);

        addressRepository.save(addressToSave);
    }
}
