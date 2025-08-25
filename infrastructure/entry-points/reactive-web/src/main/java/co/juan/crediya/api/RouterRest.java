package co.juan.crediya.api;

import co.juan.crediya.api.dto.RegisterUserDTO;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllUsers",
                    operation = @Operation(
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
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "addUser",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Created",
                                            content = @Content(
                                                    schema = @Schema(implementation = co.juan.crediya.api.dto.ApiResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Fields empty or null",
                                            content = @Content(
                                                    schema = @Schema(implementation = co.juan.crediya.api.dto.ApiResponse.class)
                                            )
                                    ),
                            },
                            requestBody = @RequestBody(
                                    content = @Content(
                                            schema = @Schema(implementation = RegisterUserDTO.class)
                                    )
                            )
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenSaveUser)
                .andRoute(GET("/api/v1/usuarios"), handler::listenGetAllUsers);
    }
}
