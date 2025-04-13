package net.mymenu.company;

import net.mymenu.address.Address;
import net.mymenu.company.dto.CompanyRequest;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.security.JwtHelper;
import jakarta.validation.Valid;
import net.mymenu.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRequest company) {
        User user = jwtHelper.extractUser();

        FileStorage image = companyService.findImage(company.getImageId());
        Address address = companyService.buildAddress(company.getAddress());

        Company newCompany = Company.builder()
                .name(company.getName())
                .cnpj(company.getCnpj())
                .email(company.getEmail())
                .primaryColor(company.getPrimaryColor())
                .phone(company.getPhone())
                .image(image)
                .address(address)
                .url(UUID.randomUUID().toString())
                .build();

        user.getCompanies().add(newCompany);

        companyRepository.saveAndFlush(newCompany);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyRequest company) {
        User user = jwtHelper.extractUser();
        Company companyToUpdate = user.getCompany();

        FileStorage image = companyService.findImage(company.getImageId());
        Address address = companyToUpdate.getAddress();

        if (address != null) {
            address.setStreet(company.getAddress().getStreet());
            address.setNumber(company.getAddress().getNumber());
            address.setComplement(company.getAddress().getComplement());
            address.setNeighborhood(company.getAddress().getNeighborhood());
            address.setCity(company.getAddress().getCity());
            address.setState(company.getAddress().getState());
            address.setZipCode(company.getAddress().getZipCode());
        } else {
            address = companyService.buildAddress(company.getAddress());
        }

        companyToUpdate.setName(company.getName());
        companyToUpdate.setCnpj(company.getCnpj());
        companyToUpdate.setEmail(company.getEmail());
        companyToUpdate.setPrimaryColor(company.getPrimaryColor());
        companyToUpdate.setPhone(company.getPhone());
        companyToUpdate.setImage(image);
        companyToUpdate.setAddress(address);

        companyRepository.saveAndFlush(companyToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
