package net.mymenu.mapper;

import net.mymenu.dto.CompanyResponse;
import net.mymenu.models.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "cnpj", constant = "***")
    @Mapping(target = "email", constant = "***")
    @Mapping(target = "phone", constant = "***")
    Company toRestrictCompany(Company company);

    @Mapping(target = "image", source = "image.url")
    @Mapping(target = "header", source = "header.url")
    CompanyResponse toCompanyResponse(Company company);
}
