package com.serkan.peri.mapstruct;


import com.serkan.peri.dto.response.hr.RecordUserResDto;
import com.serkan.peri.entity.user.*;
import org.mapstruct.Mapper;

@Mapper(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)

public interface RecordUserResDtoMapper {

    RecordUserResDto toDto(Users users);
    



}