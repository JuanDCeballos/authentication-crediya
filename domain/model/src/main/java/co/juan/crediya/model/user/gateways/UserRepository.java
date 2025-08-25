package co.juan.crediya.model.user.gateways;

import co.juan.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User user);

    Flux<User> findAll();

    Mono<Boolean> existsByEmail(String email);
}
