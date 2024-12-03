package net.mymenu.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import net.mymenu.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleMapService.class);

    private final GeoApiContext context;

    public GoogleMapService(GeoApiContext context) {
        this.context = context;
    }

    public GeocodingResult[] geocodeCEP(String cep) {
        LOGGER.info("Geocoding address for CEP: {}", cep);

        try {
            return GeocodingApi.geocode(context, cep).await();
        } catch (Exception e) {
            throw new RuntimeException("Error on geocoding address", e);
        }
    }
}
