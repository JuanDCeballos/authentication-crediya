package co.juan.crediya.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotEmpty(message = "email can't be empty")
    @Email
    private String email;
    @NotEmpty(message = "password is empty")
    private String password;
}
