package co.juan.crediya.r2dbc.service;

import co.juan.crediya.model.user.User;
import co.juan.crediya.usecase.user.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final TransactionalOperator transactionalOperator;
    private final UserUseCase userUseCase;

    public Mono<User> saveUser(User user) {
        return transactionalOperator.execute(transaction ->
                        userUseCase.saveUser(user)
                )
                .doOnNext(savedUser -> log.info("User {} saved successfully.", savedUser.getName()))
                .doOnError(throwable -> log.error("Error failed service transactional: SaveUser. {}", throwable.getMessage()))
                .single();
    }

    public Flux<User> getAllUsers() {
        return userUseCase.getAllUsers();
    }
}
