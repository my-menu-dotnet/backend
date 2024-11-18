package com.digimenu.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapService {

    private final GeoApiContext context;

    public GoogleMapService(GeoApiContext context) {
        this.context = context;
    }

    public GeocodingResult[] geocodeCEP(String cep) {
        return GeocodingApi.geocode(context, cep).awaitIgnoreError();
    }
}
