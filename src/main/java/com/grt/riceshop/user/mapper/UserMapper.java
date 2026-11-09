package com.grt.riceshop.user.mapper;

import com.grt.riceshop.user.dto.UserResponse;
import com.grt.riceshop.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="name", source="fullName")
    UserResponse mapToDTO(User user);
}
