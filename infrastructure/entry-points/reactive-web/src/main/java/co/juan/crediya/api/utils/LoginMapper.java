package co.juan.crediya.api.utils;

import co.juan.crediya.api.dto.LoginRequestDto;
import co.juan.crediya.model.dto.LogInDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    LogInDTO toLoginDto(LoginRequestDto requestDto);

    LoginRequestDto toLoginRequestDto(LogInDTO loginDto);
}
