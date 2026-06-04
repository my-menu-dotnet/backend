package net.mymenu.service;

import jakarta.transaction.Transactional;
import net.mymenu.dto.client.ClientRequest;
import net.mymenu.dto.client.ClientResponse;
import net.mymenu.exception.NotFoundException;
import net.mymenu.models.Address;
import net.mymenu.models.Client;
import net.mymenu.repository.AddressRepository;
import net.mymenu.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public List<Client> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return clientRepository.searchByName(name.trim(), Pageable.ofSize(10));
    }

    public Client findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));
    }

    @Transactional
    public Client create(ClientRequest request) {
        Address address = null;
        if (request.getAddress() != null && hasAnyAddressField(request)) {
            address = Address.builder()
                    .street(request.getAddress().getStreet())
                    .number(request.getAddress().getNumber())
                    .complement(request.getAddress().getComplement())
                    .neighborhood(request.getAddress().getNeighborhood())
                    .city(request.getAddress().getCity())
                    .state(request.getAddress().getState())
                    .zipCode(request.getAddress().getZipCode())
                    .build();
            addressRepository.saveAndFlush(address);
        }

        Client client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .cpf(request.getCpf())
                .address(address)
                .build();

        return clientRepository.saveAndFlush(client);
    }

    @Transactional
    public Client update(UUID id, ClientRequest request) {
        Client client = findById(id);

        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCpf(request.getCpf());

        if (request.getAddress() != null && hasAnyAddressField(request)) {
            Address address = client.getAddress();
            if (address == null) {
                address = Address.builder().build();
            }
            address.setStreet(request.getAddress().getStreet());
            address.setNumber(request.getAddress().getNumber());
            address.setComplement(request.getAddress().getComplement());
            address.setNeighborhood(request.getAddress().getNeighborhood());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setZipCode(request.getAddress().getZipCode());
            addressRepository.saveAndFlush(address);
            client.setAddress(address);
        }

        return clientRepository.saveAndFlush(client);
    }

    @Transactional
    public void delete(UUID id) {
        findById(id);
        clientRepository.removeById(id);
    }

    public ClientResponse toResponse(Client client) {
        ClientResponse.AddressPayload addressPayload = null;
        if (client.getAddress() != null) {
            Address address = client.getAddress();
            addressPayload = ClientResponse.AddressPayload.builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .complement(address.getComplement())
                    .neighborhood(address.getNeighborhood())
                    .city(address.getCity())
                    .state(address.getState())
                    .zipCode(address.getZipCode())
                    .build();
        }

        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .cpf(client.getCpf())
                .address(addressPayload)
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }

    private boolean hasAnyAddressField(ClientRequest request) {
        var a = request.getAddress();
        return notBlank(a.getStreet()) || notBlank(a.getNumber())
                || notBlank(a.getNeighborhood()) || notBlank(a.getCity())
                || notBlank(a.getState()) || notBlank(a.getZipCode());
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
