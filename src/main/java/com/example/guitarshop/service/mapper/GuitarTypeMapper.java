package com.example.guitarshop.service.mapper;

import com.example.guitarshop.domain.GuitarType;
import com.example.guitarshop.service.dto.GuitarTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GuitarType} and its DTO {@link GuitarTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface GuitarTypeMapper extends EntityMapper<GuitarTypeDTO, GuitarType> {}
