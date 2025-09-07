package co.juan.crediya.r2dbc.entity;

import co.juan.crediya.constants.UserRoles;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity implements UserDetails {

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

    private String dni;

    @Column("id_rol")
    private Long role;

    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = mapRoleIdToRoleName(role);
        return Stream.of(roleName).map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private String mapRoleIdToRoleName(Long roleId) {
        return UserRoles.fromId(roleId).getNameRole();
    }
}
