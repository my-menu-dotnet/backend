package net.mymenu.analytics.company_access;

import lombok.*;
import net.mymenu.analytics.company_access.enums.AccessWays;
import net.mymenu.analytics.company_access.enums.ContactWays;
import jakarta.persistence.*;
import net.mymenu.tenant.BaseEntity;

import java.time.LocalDateTime;

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
