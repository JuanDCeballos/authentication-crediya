package co.juan.crediya.model.user.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(409, "Email already exist."),
    INVALID_BASE_SALARY(422, "The base salary is not within range."),
    USER_NOT_FOUND(404, "The user with dni {} doesn't exists."),
    DNI_ALREADY_EXISTS(409, "DNI already exist.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
