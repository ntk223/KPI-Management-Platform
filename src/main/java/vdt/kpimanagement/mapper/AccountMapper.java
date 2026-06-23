package vdt.kpimanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vdt.kpimanagement.common.GenericMapper;
import vdt.kpimanagement.dto.AccountRequest;
import vdt.kpimanagement.dto.AccountResponse;
import vdt.kpimanagement.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper extends GenericMapper<Account, AccountRequest, AccountResponse> {

    @Override
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.employeeCode", target = "employeeCode")
    @Mapping(source = "employee.fullName", target = "fullName")
    @Mapping(source = "employee.email", target = "email")
    @Mapping(target = "roles", ignore = true) // Được set thủ công trong Service
    AccountResponse toDto(Account entity);

    @Override
    @Mapping(target = "employee", ignore = true)   // set thủ công trong Service
    @Mapping(target = "password", ignore = true)   // password cần hash trước khi set
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(AccountRequest dto);

    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(AccountRequest dto, @MappingTarget Account entity);
}
