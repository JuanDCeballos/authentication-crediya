package co.juan.crediya.api;

import co.juan.crediya.api.dto.ApiResponse;
import co.juan.crediya.api.dto.RegisterUserDTO;
import co.juan.crediya.api.utils.UserMapper;
import co.juan.crediya.api.utils.ValidationService;
import co.juan.crediya.model.user.User;
import co.juan.crediya.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final UserService userService;
    private final ValidationService validationService;
    private final UserMapper userMapper;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserDTO.class)
                .flatMap(validationService::validateObject)
                .map(userMapper::toUser)
                .flatMap(userService::saveUser)
                .flatMap(savedUser -> {
                    ApiResponse<User> response = new ApiResponse<>(
                            201,
                            "User created successfully",
                            serverRequest.path(),
                            "Created",
                            savedUser
                    );

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userService.getAllUsers(), User.class);
    }
}
