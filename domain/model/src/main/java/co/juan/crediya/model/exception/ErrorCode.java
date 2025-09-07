package co.juan.crediya.model.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("BEC_AE_001", "Email already exist.", 409),
    INVALID_BASE_SALARY("BEC_SNR", "The base salary is not within range.", 422),
    USER_NOT_FOUND("BEC_NF", "The user with dni doesn't exists.", 404),
    DNI_ALREADY_EXISTS("BEC_AE_002", "DNI already exist.", 409),
    ROLE_NOT_FOUND("BEC_RNF", "DNI already exist.", 409),
    NO_TOKE_FOUND("BEC_NTF", "No token was found in the request.", 404),
    BAD_TOKEN("BEC_BT", "No token has been sent in the request.", 403),
    INVALID_TOKEN("BEC_IT", "Invalid authentication in the request.", 403),
    BAD_CREDENTIALS("BEC_BC", "Bad credentials", 401);


    private final String businessErrorCode;
    private final String message;
    private final Integer httpCode;

    ErrorCode(String businessErrorCode, String message, Integer httpCode) {
        this.businessErrorCode = businessErrorCode;
        this.message = message;
        this.httpCode = httpCode;
    }
}
