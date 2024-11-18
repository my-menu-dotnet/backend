package com.digimenu.mapper;

import com.digimenu.models.Company;
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

    @Mapping(target = ".", qualifiedByName = "toRestrictCompany")
    List<Company> toRestrictListCompany(List<Company> company);

}
