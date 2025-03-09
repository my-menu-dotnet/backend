package net.mymenu.controllers;

import com.google.maps.model.GeocodingResult;
import net.mymenu.constraints.CEP;
import net.mymenu.dto.address.AddressResponse;
import net.mymenu.mapper.AddressMapper;
import net.mymenu.models.Address;
import net.mymenu.repository.AddressRepository;
import net.mymenu.service.AddressService;
import net.mymenu.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<Address> getAddress(@RequestParam @CEP String cep) {
        Address address = addressRepository.findCoordsByZipCodeAndValidatedTrue(cep)
                .orElse(null);

        if (address != null) {
            address.setNumber(null);
            return ResponseEntity.ok(address);
        }

        Address newAddress = addressService.getAddressByCep(cep);
        if (newAddress != null) {
            newAddress.setValidated(true);

            addressRepository.save(newAddress);

            return ResponseEntity.ok(newAddress);
        }

        return ResponseEntity.noContent().build();
    }
}
