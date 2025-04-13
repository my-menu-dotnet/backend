package net.mymenu.config;

import net.mymenu.user.User;
import net.mymenu.user.UserRepository;
import net.mymenu.user.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.UUID;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public UserRepository userRepository() {
        UserRepository repository = Mockito.mock(UserRepository.class);
        User testUser = User.builder()
                .id(UUID.randomUUID())
                .email("admin@test.com")
                .password("password")
                .isActive(true)
                .build();

        Mockito.when(repository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(testUser));

        return repository;
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        UserService service = new UserService();
        service.userRepository = userRepository;
        return service;
    }
}
