package net.mymenu.service;

import net.mymenu.dto.BusinessHoursRequest;
import net.mymenu.dto.BusinessHoursResponse;
import net.mymenu.dto.WeeklyBusinessHoursRequest;
import net.mymenu.models.BusinessHours;
import net.mymenu.models.Company;
import net.mymenu.repository.BusinessHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessHoursService {

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    public List<BusinessHoursResponse> getBusinessHoursByCompany(Company company) {
        List<BusinessHours> businessHours = businessHoursRepository.findByCompanyOrderByDayOfWeek(company);
        return businessHours.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BusinessHoursResponse> updateWeeklyBusinessHours(Company company, WeeklyBusinessHoursRequest request) {
        // Remove todos os horários existentes primeiro
        List<BusinessHours> existingHours = businessHoursRepository.findByCompanyOrderByDayOfWeek(company);
        if (!existingHours.isEmpty()) {
            businessHoursRepository.deleteAll(existingHours);
            businessHoursRepository.flush(); // Força a execução do DELETE antes de continuar
        }

        List<BusinessHours> newBusinessHours = new ArrayList<>();

        for (BusinessHoursRequest hourRequest : request.getBusinessHours()) {
            BusinessHours businessHour = BusinessHours.builder()
                    .company(company)
                    .dayOfWeek(hourRequest.getDayOfWeek())
                    .openingTime(hourRequest.getOpeningTime())
                    .closingTime(hourRequest.getClosingTime())
                    .isClosed(hourRequest.isClosed())
                    .build();

            // Validação: se está fechado, não deve ter horários
            if (businessHour.isClosed()) {
                businessHour.setOpeningTime(null);
                businessHour.setClosingTime(null);
            }

            newBusinessHours.add(businessHour);
        }

        List<BusinessHours> savedBusinessHours = businessHoursRepository.saveAll(newBusinessHours);

        return savedBusinessHours.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public BusinessHoursResponse updateSingleBusinessHour(Company company, DayOfWeek dayOfWeek, BusinessHoursRequest request) {
        BusinessHours businessHour = businessHoursRepository.findByCompanyAndDayOfWeek(company, dayOfWeek)
                .orElse(BusinessHours.builder()
                        .company(company)
                        .dayOfWeek(dayOfWeek)
                        .build());

        businessHour.setOpeningTime(request.getOpeningTime());
        businessHour.setClosingTime(request.getClosingTime());
        businessHour.setClosed(request.isClosed());

        // Validação: se está fechado, não deve ter horários
        if (businessHour.isClosed()) {
            businessHour.setOpeningTime(null);
            businessHour.setClosingTime(null);
        }

        BusinessHours savedBusinessHour = businessHoursRepository.save(businessHour);
        return convertToResponse(savedBusinessHour);
    }

    private BusinessHoursResponse convertToResponse(BusinessHours businessHours) {
        return new BusinessHoursResponse(
                businessHours.getId(),
                businessHours.getDayOfWeek(),
                businessHours.getOpeningTime(),
                businessHours.getClosingTime(),
                businessHours.isClosed()
        );
    }
}
