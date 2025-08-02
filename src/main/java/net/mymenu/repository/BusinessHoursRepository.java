package net.mymenu.repository;

import net.mymenu.models.BusinessHours;
import net.mymenu.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHours, UUID> {

    List<BusinessHours> findByCompanyOrderByDayOfWeek(Company company);

    Optional<BusinessHours> findByCompanyAndDayOfWeek(Company company, DayOfWeek dayOfWeek);

    void deleteByCompany(Company company);
}
