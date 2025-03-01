package net.mymenu.models.analytics;

import lombok.*;
import net.mymenu.enums.analytics.AccessWays;
import net.mymenu.enums.analytics.ContactWays;
import net.mymenu.models.Company;
import jakarta.persistence.*;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "analytic_company_access")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyAccess extends BaseEntity {

    @Column(name = "ip")
    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "access_way")
    @Enumerated(EnumType.STRING)
    private AccessWays accessWay;

    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;

    @Column(name = "time_on_page")
    private Integer timeOnPage;

    @Column(name = "contacted")
    private Boolean contacted;

    @Column(name = "contacted_at")
    private LocalDateTime contactedAt;

    @Column(name = "contacted_from")
    @Enumerated(EnumType.STRING)
    private ContactWays contactedFrom;
}
