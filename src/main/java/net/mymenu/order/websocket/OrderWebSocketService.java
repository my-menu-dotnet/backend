package net.mymenu.order.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class OrderWebSocketService {

    // Map tenant ID to set of user sessions
    private final ConcurrentHashMap<UUID, Set<String>> tenantSessions = new ConcurrentHashMap<>();
    // Keep track of which tenant each session belongs to
    private final ConcurrentHashMap<String, UUID> sessionTenant = new ConcurrentHashMap<>();
    // Original user session tracking
    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void registerUserSession(String userId, String sessionId, UUID tenantId) {
        userSessions.put(userId, sessionId);
        sessionTenant.put(sessionId, tenantId);

        tenantSessions.computeIfAbsent(tenantId, k -> new ConcurrentSkipListSet<>())
                .add(sessionId);
    }

    public void sendNotificationToUser(String userId, String notification) {
        String sessionId = userSessions.get(userId);
        if (sessionId != null) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/notifications", notification);
        }
    }

    public void sendNotificationToTenant(UUID tenantId, Object notification) {
        Set<String> sessions = tenantSessions.get(tenantId);
        if (sessions != null) {
            messagingTemplate.convertAndSend("/topic/orders/" + tenantId, notification);
        }
    }

    public void removeUserSession(String userId) {
        String sessionId = userSessions.remove(userId);
        if (sessionId != null) {
            UUID tenantId = sessionTenant.remove(sessionId);
            if (tenantId != null) {
                Set<String> sessions = tenantSessions.get(tenantId);
                if (sessions != null) {
                    sessions.remove(sessionId);
                    if (sessions.isEmpty()) {
                        tenantSessions.remove(tenantId);
                    }
                }
            }
        }
    }
}