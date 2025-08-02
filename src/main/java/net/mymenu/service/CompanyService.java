package net.mymenu.service;

import net.mymenu.dto.AddressRequest;
import net.mymenu.dto.BusinessHoursRequest;
import net.mymenu.dto.BusinessHoursResponse;
import net.mymenu.dto.CompanyResponse;
import net.mymenu.dto.address.AddressResponse;
import net.mymenu.models.Address;
import net.mymenu.models.BusinessHours;
import net.mymenu.models.Category;
import net.mymenu.models.Company;
import net.mymenu.models.FileStorage;
import net.mymenu.repository.CategoryRepository;
import net.mymenu.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private BusinessHoursService businessHoursService;

    @Value("${frontend.url}")
    private String frontendUrl;

    public List<Category> findCategories(List<UUID> categoryIds) {
        return categoryRepository.findAllByIdList(categoryIds)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public FileStorage findImage(UUID imageId) {
        return fileStorageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public Address buildAddress(AddressRequest addressRequest) {
        return Address.builder()
                .street(addressRequest.getStreet())
                .number(addressRequest.getNumber())
                .complement(addressRequest.getComplement())
                .neighborhood(addressRequest.getNeighborhood())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .build();
    }

    public byte[] generateQrCode(UUID companyId, String logoName) throws Exception {
        String url = frontendUrl + "/" + companyId;
        return QRCodeService.generateQRCodeImageBytes(url, logoName);
    }

    public CompanyResponse convertToResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setDescription(company.getDescription());
        response.setCnpj(company.getCnpj());
        response.setPhone(company.getPhone());
        response.setEmail(company.getEmail());
        response.setUrl(company.getUrl());
        response.setPrimaryColor(company.getPrimaryColor());
        response.setVerifiedEmail(company.isVerifiedEmail());
        response.setDelivery(company.isDelivery());
        response.setDeliveryPrice(company.getDeliveryPrice());
        response.setDeliveryRadius(company.getDeliveryRadius());
        response.setStatus(company.getStatus());
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());

        // Converter imagens
        if (company.getImage() != null) {
            response.setImage(company.getImage().getFileName());
        }
        if (company.getHeader() != null) {
            response.setHeader(company.getHeader().getFileName());
        }

        // Converter endereço
        if (company.getAddress() != null) {
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setStreet(company.getAddress().getStreet());
            addressResponse.setNumber(company.getAddress().getNumber());
            addressResponse.setComplement(company.getAddress().getComplement());
            addressResponse.setNeighborhood(company.getAddress().getNeighborhood());
            addressResponse.setCity(company.getAddress().getCity());
            addressResponse.setState(company.getAddress().getState());
            addressResponse.setZipCode(company.getAddress().getZipCode());
            response.setAddress(addressResponse);
        }

        // Converter horários de funcionamento
        if (company.getBusinessHours() != null && !company.getBusinessHours().isEmpty()) {
            List<BusinessHoursResponse> businessHoursResponses = company.getBusinessHours().stream()
                    .map(this::convertBusinessHoursToResponse)
                    .collect(Collectors.toList());
            response.setBusinessHours(businessHoursResponses);
        }

        return response;
    }

    private BusinessHoursResponse convertBusinessHoursToResponse(BusinessHours businessHours) {
        return new BusinessHoursResponse(
                businessHours.getId(),
                businessHours.getDayOfWeek(),
                businessHours.getOpeningTime(),
                businessHours.getClosingTime(),
                businessHours.isClosed()
        );
    }

    public void updateBusinessHours(Company company, List<BusinessHoursRequest> businessHoursRequests) {
        if (businessHoursRequests != null && !businessHoursRequests.isEmpty()) {
            // Remove horários existentes
            if (company.getBusinessHours() != null) {
                company.getBusinessHours().clear();
            }

            // Adiciona novos horários
            for (BusinessHoursRequest request : businessHoursRequests) {
                BusinessHours businessHours = BusinessHours.builder()
                        .company(company)
                        .dayOfWeek(request.getDayOfWeek())
                        .openingTime(request.getOpeningTime())
                        .closingTime(request.getClosingTime())
                        .isClosed(request.isClosed())
                        .build();

                // Validação: se está fechado, não deve ter horários
                if (businessHours.isClosed()) {
                    businessHours.setOpeningTime(null);
                    businessHours.setClosingTime(null);
                }

                if (company.getBusinessHours() == null) {
                    company.setBusinessHours(new java.util.ArrayList<>());
                }
                company.getBusinessHours().add(businessHours);
            }
        }
    }
}
