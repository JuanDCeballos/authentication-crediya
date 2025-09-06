package co.juan.crediya.api;

import co.juan.crediya.api.config.AuthPath;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final AuthPath authPath;

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/usuarios", method = RequestMethod.POST, beanClass = Handler.class, beanMethod = "listenSaveUser"),
            @RouterOperation(path = "/api/v1/usuarios", method = RequestMethod.GET, beanClass = Handler.class, beanMethod = "listenGetAllUsers"),
            @RouterOperation(path = "/api/v1/usuarios/dni/{dni}", method = RequestMethod.GET, beanClass = Handler.class, beanMethod = "listenGetUserEmailByDni"),
            @RouterOperation(path = "/api/v1/login", method = RequestMethod.POST, beanClass = Handler.class, beanMethod = "listenPostLogin")
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(authPath.getVersion() + authPath.getUsuarios()), handler::listenSaveUser)
                .andRoute(GET(authPath.getVersion() + authPath.getUsuarios()), handler::listenGetAllUsers)
                .andRoute(GET(authPath.getVersion() + authPath.getUsuarioByDni()), handler::listenGetUserByDni)
                .andRoute(POST(authPath.getVersion() + authPath.getLogin()), handler::listenPostLogin)
                .andRoute(GET(authPath.getVersion() + authPath.getUsuarioByEmail()), handler::listenGetUserByEmail);
    }
}
