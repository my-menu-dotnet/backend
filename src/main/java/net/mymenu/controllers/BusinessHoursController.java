package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.dto.BusinessHoursRequest;
import net.mymenu.dto.BusinessHoursResponse;
import net.mymenu.dto.WeeklyBusinessHoursRequest;
import net.mymenu.models.Company;
import net.mymenu.models.User;
import net.mymenu.security.JwtHelper;
import net.mymenu.service.BusinessHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/company/business-hours")
public class BusinessHoursController {

    @Autowired
    private BusinessHoursService businessHoursService;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusinessHoursResponse>> getBusinessHours() {
        User user = jwtHelper.extractUser();
        Company company = user.getCompany();

        List<BusinessHoursResponse> businessHours = businessHoursService.getBusinessHoursByCompany(company);
        return ResponseEntity.ok(businessHours);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusinessHoursResponse>> updateWeeklyBusinessHours(
            @Valid @RequestBody WeeklyBusinessHoursRequest request) {
        User user = jwtHelper.extractUser();
        Company company = user.getCompany();

        List<BusinessHoursResponse> updatedBusinessHours = businessHoursService.updateWeeklyBusinessHours(company, request);
        return ResponseEntity.ok(updatedBusinessHours);
    }

    @PutMapping("/{dayOfWeek}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessHoursResponse> updateSingleBusinessHour(
            @PathVariable DayOfWeek dayOfWeek,
            @Valid @RequestBody BusinessHoursRequest request) {
        User user = jwtHelper.extractUser();
        Company company = user.getCompany();

        BusinessHoursResponse updatedBusinessHour = businessHoursService.updateSingleBusinessHour(company, dayOfWeek, request);
        return ResponseEntity.ok(updatedBusinessHour);
    }
}
