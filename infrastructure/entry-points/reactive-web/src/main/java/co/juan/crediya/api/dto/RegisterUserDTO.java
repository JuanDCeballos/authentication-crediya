package co.juan.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDTO {

    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotEmpty(message = "LastName can't be empty")
    private String lastName;
    private Date birthDate;
    private String address;
    private String phone;
    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;
    @NotNull(message = "baseSalary can't be null")
    private BigDecimal baseSalary;
}
