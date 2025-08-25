package co.juan.crediya.api.utils;

import co.juan.crediya.api.dto.RegisterUserDTO;
import co.juan.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterUserDTO userDto);

    RegisterUserDTO toUserDto(User user);
}
