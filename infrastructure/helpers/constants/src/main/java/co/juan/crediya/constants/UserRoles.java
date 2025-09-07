package co.juan.crediya.constants;

import co.juan.crediya.model.exception.CrediYaException;
import co.juan.crediya.model.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum UserRoles {
    ADMIN(1L, "ADMIN"),
    ADVISOR(2L, "ADVISOR"),
    CUSTOMER(3L, "CUSTOMER"),;

    private final Long id;
    private final String nameRole;

    UserRoles(Long id, String nameRole) {
        this.id = id;
        this.nameRole = nameRole;
    }

    public static UserRoles fromId(Long id) {
        for (UserRoles role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        throw new CrediYaException(ErrorCode.ROLE_NOT_FOUND);
    }
}
