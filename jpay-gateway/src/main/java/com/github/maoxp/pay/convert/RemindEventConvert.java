package com.github.maoxp.pay.convert;


import com.github.maoxp.pay.client.remind.dto.RemindEventDTO;
import com.github.maoxp.service.entity.BwRemindEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Objects;

/**
 * RemindEventConvert
 *
 * @author mxp
 * @date 2023年06月01日 14:23
 * @since JDK 1.8
 */
@Mapper(componentModel = "spring")
public interface RemindEventConvert {
    @Named("getExpiredFlag")
    default String getExpiredFlag(RemindEventDTO remindEventDTO) {
        if (Objects.nonNull(remindEventDTO.getExpiredDate()) && Objects.nonNull(remindEventDTO.getExpiredTime())) {
            return "1";
        } else if (Objects.nonNull(remindEventDTO.getExpiredDate())) {
            return "2";
        } else if (Objects.nonNull(remindEventDTO.getExpiredTime())) {
            return "3";
        } else {
            throw new RuntimeException("getExpiredFlag exception");
        }
    }

    @Mappings({
            @Mapping(target = "expiredFlag", source = "remindEventDTO", qualifiedByName = "getExpiredFlag"),
//            @Mapping(target = "expiredFlag", expression = "java(getExpiredFlag(remindEventDTO))"),
            @Mapping(target = "createTime", source = ""),
            @Mapping(target = "updateTime", source = ""),
    })
    BwRemindEvent remindEventDTOTransFormBwRemindEvent(RemindEventDTO remindEventDTO);
}
