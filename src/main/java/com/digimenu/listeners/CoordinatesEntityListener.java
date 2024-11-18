package com.digimenu.listeners;

import com.digimenu.models.Address;
import com.digimenu.service.AddressAsyncService;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CoordinatesEntityListener {

    @Lazy
    @Autowired
    private AddressAsyncService addressAsyncService;

    @PrePersist
    @PreUpdate
    public void addCoordinates(Object entity) {
        if (!(entity instanceof Address address)) {
            throw new IllegalArgumentException("Entity is not an instance of Address");
        }

        addressAsyncService.addLocation(address.getZipCode(), address);
    }
}
