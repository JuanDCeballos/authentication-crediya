package co.juan.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @Column("id")
    private Long id;
    private String name;

    @Column("lastname")
    private String lastName;

    @Column("birthdate")
    private LocalDateTime birthDate;
    private String address;
    private String phone;
    private String email;

    @Column("basesalary")
    private BigDecimal baseSalary;
}
