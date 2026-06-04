package net.mymenu.controllers;

import jakarta.validation.Valid;
import net.mymenu.dto.client.ClientRequest;
import net.mymenu.dto.client.ClientResponse;
import net.mymenu.models.Client;
import net.mymenu.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientResponse>> list(Pageable pageable) {
        Page<Client> clients = clientService.findAll(pageable);
        Page<ClientResponse> responses = clients.map(clientService::toResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientResponse>> search(@RequestParam("name") String name) {
        List<ClientResponse> responses = clientService.searchByName(name).stream()
                .map(clientService::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> get(@PathVariable UUID id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(clientService.toResponse(client));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        Client client = clientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.toResponse(client));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequest request
    ) {
        Client client = clientService.update(id, request);
        return ResponseEntity.ok(clientService.toResponse(client));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
