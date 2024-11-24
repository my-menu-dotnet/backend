package com.digimenu.models.analytics;

import com.digimenu.enums.analytics.AccessWays;
import com.digimenu.enums.analytics.ContactWays;
import com.digimenu.models.Company;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "analytic_company_access")
@Entity
public class CompanyAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
