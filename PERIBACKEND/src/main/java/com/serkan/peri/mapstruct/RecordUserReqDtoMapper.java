package com.serkan.peri.mapstruct;


import com.serkan.peri.configuration.exceptionconfiguration.ApplicationSpesificException;
import com.serkan.peri.configuration.exceptionconfiguration.ErrorType;
import com.serkan.peri.dto.request.hr.RecordUserReqDto;
import com.serkan.peri.entity.user.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;




@Mapper(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RecordUserReqDtoMapper {

    RecordUserReqDto toDto(Users users);
    @Mapping(target = "userCategories", source = "userCategories")
    Users toEntity(RecordUserReqDto recordUserReqDto);


    @ObjectFactory
    default Users createUser(RecordUserReqDto recordUserReqDto) {
        if (recordUserReqDto.userCategories() == null) {
            throw new ApplicationSpesificException(ErrorType.PARAMETRE_HATASI);
        }

        // 1. Önce nesneyi tipine göre oluştur
        Users users = switch (recordUserReqDto.userCategories()) {

            case EXECUTIVE, BOARD_MEMBER, REGIONAL_MANAGER -> new ExecutiveUsers();
            case TECH_IT, DATA_ANALYTICS, CYBER_SECURITY -> new TechnicalUsers();
            case SUPPLY_CHAIN, MANUFACTURING, QUALITY_CONTROL -> new OperationsUsers();
            case GENERAL_LABOR, WAREHOUSE_STAFF, DISTRIBUTION_ENTRY, FACILITY_MAINTENANCE, SEASONAL_WORKER, FIELD_OPERATIVE -> new FrontLineUsers();
            case SALES_RETAIL, MARKETING_BRAND, PUBLIC_RELATIONS -> new CommercialUsers();
            case HUMAN_RESOURCES, FINANCE_TREASURY, LEGAL_COMPLIANCE, SUSTAINABILITY, DEPARTMENT_HEAD, STRATEGIC_PLANNING, RESEARCH_DEV -> new CorporateUsers();
            case CONTRACTOR -> new ContractorUsers();
            case DEMO_USER -> new DemoUsers();
            default -> throw new ApplicationSpesificException(ErrorType.INVALID_USER_CATEGORY);
        };

        // 2. KRİTİK ADIM: Eksik olan alanı burada manuel set et
        users.setUserCategories(recordUserReqDto.userCategories());

        return users;
    }
    }
