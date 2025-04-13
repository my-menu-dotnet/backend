package net.mymenu.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "cnpj", constant = "***")
    @Mapping(target = "email", constant = "***")
    @Mapping(target = "phone", constant = "***")
    Company toRestrictCompany(Company company);
}
