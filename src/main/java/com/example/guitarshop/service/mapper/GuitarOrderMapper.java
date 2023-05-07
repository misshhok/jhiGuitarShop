package com.example.guitarshop.service.mapper;

import com.example.guitarshop.domain.Guitar;
import com.example.guitarshop.domain.GuitarOrder;
import com.example.guitarshop.domain.User;
import com.example.guitarshop.service.dto.GuitarDTO;
import com.example.guitarshop.service.dto.GuitarOrderDTO;
import com.example.guitarshop.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GuitarOrder} and its DTO {@link GuitarOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface GuitarOrderMapper extends EntityMapper<GuitarOrderDTO, GuitarOrder> {
    @Mapping(target = "orderOwner", source = "orderOwner", qualifiedByName = "userLogin")
    @Mapping(target = "guitarsInOrders", source = "guitarsInOrders", qualifiedByName = "guitarIdSet")
    GuitarOrderDTO toDto(GuitarOrder s);

    @Mapping(target = "removeGuitarsInOrder", ignore = true)
    GuitarOrder toEntity(GuitarOrderDTO guitarOrderDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("guitarId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GuitarDTO toDtoGuitarId(Guitar guitar);

    @Named("guitarIdSet")
    default Set<GuitarDTO> toDtoGuitarIdSet(Set<Guitar> guitar) {
        return guitar.stream().map(this::toDtoGuitarId).collect(Collectors.toSet());
    }
}
