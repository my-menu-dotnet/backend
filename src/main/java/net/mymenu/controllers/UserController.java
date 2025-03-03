package net.mymenu.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.mymenu.dto.user.UserRequest;
import net.mymenu.models.Address;
import net.mymenu.models.User;
import net.mymenu.repository.AddressRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    public ResponseEntity<User> getUserByToken() {
        User user = jwtHelper.extractUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<User> updateUserByToken(@Valid @RequestBody UserRequest userRequest) {
        User user = jwtHelper.extractUser();

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getCpf() != null) {
            user.setCpf(userRequest.getCpf());
        }
        if (userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
        }

        if (userRequest.getAddress() != null) {
            Address address = Address
                    .builder()
                    .street(userRequest.getAddress().getStreet())
                    .number(userRequest.getAddress().getNumber())
                    .complement(userRequest.getAddress().getComplement())
                    .neighborhood(userRequest.getAddress().getNeighborhood())
                    .city(userRequest.getAddress().getCity())
                    .state(userRequest.getAddress().getState())
                    .zipCode(userRequest.getAddress().getZipCode())
                    .build();

            addressRepository.saveAndFlush(address);

            user.setAddress(address);
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
