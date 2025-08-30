package co.juan.crediya.api;

import co.juan.crediya.api.dto.ApiResponseDTO;
import co.juan.crediya.api.dto.RegisterUserDTO;
import co.juan.crediya.api.utils.UserMapper;
import co.juan.crediya.api.utils.ValidationService;
import co.juan.crediya.constants.OperationMessages;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.exception.CrediYaException;
import co.juan.crediya.model.user.exception.ErrorCode;
import co.juan.crediya.r2dbc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userService.getAllUsers(), User.class);
    }

    @Operation(
            operationId = "getUserEmailByDni",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get user email from user dni successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDTO.class)
                            )
                    )
            }
    )
    public Mono<ServerResponse> listenGetUserEmailByDni(ServerRequest request) {
        String dni = request.pathVariable("dni");
        return userService.getUserEmailByDni(dni)
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(userEmail -> {
                    ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                            .status("200")
                            .message(OperationMessages.USER_FOUND.getMessage())
                            .data(userEmail).build();
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }
}
