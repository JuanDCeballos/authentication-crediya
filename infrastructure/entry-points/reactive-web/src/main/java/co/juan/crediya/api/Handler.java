package co.juan.crediya.api;

import co.juan.crediya.api.dto.ApiResponseDTO;
import co.juan.crediya.api.dto.LoginRequestDto;
import co.juan.crediya.api.dto.RegisterUserDTO;
import co.juan.crediya.api.utils.LoginMapper;
import co.juan.crediya.api.utils.UserMapper;
import co.juan.crediya.api.utils.ValidationService;
import co.juan.crediya.constants.OperationMessages;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.exception.CrediYaException;
import co.juan.crediya.model.exception.ErrorCode;
import co.juan.crediya.r2dbc.service.UserService;
import co.juan.crediya.usecase.login.LogInUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final UserService userService;
    private final ValidationService validationService;
    private final UserMapper userMapper;
    private final LogInUseCase logInUseCase;
    private final LoginMapper loginMapper;

    @Operation(
            operationId = "saveUser",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Fields empty or null",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
            },
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = RegisterUserDTO.class)
                    )
            )
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ADVISOR')")
    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserDTO.class)
                .flatMap(validationService::validateObject)
                .doOnNext(req -> log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), req.toString()))
                .map(userMapper::toUser)
                .flatMap(userService::saveUser)
                .flatMap(savedUser -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("201")
                            .message(OperationMessages.RECORD_CREATED_SUCCESSFULLY.getMessage())
                            .data(savedUser).build();

                    return ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    @Operation(
            operationId = "getAllUsers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get all users successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = RegisterUserDTO.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ADVISOR')")
    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userService.getAllUsers(), User.class);
    }

    @Operation(
            operationId = "getUserByDni",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get user by dni successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADVISOR')")
    public Mono<ServerResponse> listenGetUserByDni(ServerRequest request) {
        String dni = request.pathVariable("dni");
        return userService.findUserByDni(dni)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("200")
                            .message(OperationMessages.USER_FOUND.getMessage())
                            .data(user).build();
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }

    @Operation(
            operationId = "getUserByEmail",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get user by email successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADVISOR')")
    public Mono<ServerResponse> listenGetUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return userService.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("200")
                            .message(OperationMessages.USER_FOUND.getMessage())
                            .data(user).build();
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }

    @Operation(
            operationId = "loginUser",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged successful",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "No token was found in the request.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credentials don't match.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    ),
            },
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = LoginRequestDto.class)
                    )
            )
    )
    public Mono<ServerResponse> listenPostLogin(ServerRequest request) {
        return request.bodyToMono(LoginRequestDto.class)
                .doOnNext(loginDto -> log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), loginDto.toString()))
                .flatMap(validationService::validateObject)
                .map(loginMapper::toLoginDto)
                .flatMap(logInUseCase::login)
                .flatMap(token -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("200")
                            .message(OperationMessages.LOGIN_OK.getMessage())
                            .data(token).build();
                    return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }
}
