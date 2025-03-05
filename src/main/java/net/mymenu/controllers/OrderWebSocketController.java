package net.mymenu.controllers;

import lombok.extern.slf4j.Slf4j;
import net.mymenu.models.User;
import net.mymenu.repository.OrderRepository;
import net.mymenu.repository.UserRepository;
import net.mymenu.service.OrderWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.UUID;

@Controller
@Slf4j
public class OrderWebSocketController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderWebSocketService orderWebSocketService;

    @Autowired
    private UserRepository userRepository;

    @SubscribeMapping("/orders")
    public void subscribeToTenantOrders(Principal principal, @Header("simpSessionId") String sessionId) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new SecurityException("Usuário não encontrado"));
        UUID tenantId = user.getCompany().getId();

        if (!user.getCompany().getId().equals(tenantId)) {
            throw new SecurityException("Não autorizado para acessar este tenant");
        }

        orderWebSocketService.registerUserSession(user.getId().toString(), sessionId, tenantId);
    }

    @SubscribeMapping("/orders/{tenantId}")
    public void subscribeTenantNotifications(Principal principal, @PathVariable UUID tenantId) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new SecurityException("Usuário não encontrado"));
        UUID userTenantId = user.getCompany().getId();

        if (!tenantId.equals(userTenantId)) {
            throw new SecurityException("Não autorizado para acessar este tenant");
        }
    }
}