package com.example.guitarshop.service.mapper;

import com.example.guitarshop.domain.Guitar;
import com.example.guitarshop.domain.GuitarType;
import com.example.guitarshop.service.dto.GuitarDTO;
import com.example.guitarshop.service.dto.GuitarTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Guitar} and its DTO {@link GuitarDTO}.
 */
@Mapper(componentModel = "spring")
public interface GuitarMapper extends EntityMapper<GuitarDTO, Guitar> {
    @Mapping(target = "guitarType", source = "guitarType", qualifiedByName = "guitarTypeId")
    GuitarDTO toDto(Guitar s);

    @Named("guitarTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GuitarTypeDTO toDtoGuitarTypeId(GuitarType guitarType);
}
