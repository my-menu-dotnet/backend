package net.mymenu.repository;

import net.mymenu.config.TenantTest;
import net.mymenu.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class ClientRepositoryTest extends TenantTest<Client> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    protected Client getEntity() {
        return Client.builder()
                .name("Client to Delete")
                .email("client@test.com")
                .phone("11999999999")
                .build();
    }

    @Override
    protected JpaRepository<Client, UUID> getRepository() {
        return clientRepository;
    }
}
