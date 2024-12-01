package net.mymenu.models;

import net.mymenu.constraints.CPF;
import net.mymenu.constraints.FullName;
import net.mymenu.constraints.Phone;
import net.mymenu.enums.UserRole;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
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
    private String email;

    @Column(name = "cpf")
    @CPF
    private String cpf;

    @Column(name = "phone")
    @Phone
    private String phone;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_company",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<Company> companies;

    @Column(name = "is_verified")
    private boolean isVerifiedEmail;

    @Column(name = "is_active")
    @JsonIgnore
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
}
