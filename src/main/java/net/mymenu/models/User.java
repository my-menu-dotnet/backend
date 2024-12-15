package net.mymenu.models;

import net.mymenu.constraints.*;
import net.mymenu.enums.UserRole;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_cpf", columnList = "cpf"),
        @Index(name = "idx_user_phone", columnList = "phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners({TimestampedListener.class})
public class User implements UserDetails, Timestamped {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    @FullName
    private String name;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "cpf")
    @CPF
    private String cpf;

    @Column(name = "phone")
    @Phone
    private String phone;

    @Column(name = "password")
    @JsonIgnore
    @Password
    private String password;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Company> companies;

    @Column(name = "is_verified_email")
    @ColumnDefault("false")
    private boolean isVerifiedEmail;

    @Column(name = "is_active")
    @JsonIgnore
    @ColumnDefault("true")
    private boolean isActive;

    @Column(name = "last_password_reset")
    @JsonIgnore
    private LocalDateTime lastPasswordReset;

    @Column(name = "last_login")
    @JsonIgnore
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(String name, String email, String cpf, String phone, String password) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phone = phone;
        this.password = password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.companies == null || this.companies.isEmpty()) {
            return List.of(new Role());
        }
        return List.of(new Role(UserRole.ADMIN));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isActive;
    }

    public boolean isVerifiedEmail() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        this.isActive = true;
        this.isVerifiedEmail = false;
    }
}
