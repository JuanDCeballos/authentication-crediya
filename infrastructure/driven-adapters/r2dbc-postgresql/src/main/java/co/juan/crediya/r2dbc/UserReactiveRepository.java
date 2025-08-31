package co.juan.crediya.r2dbc;

import co.juan.crediya.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByEmail(String email);

    @Query("SELECT email FROM users WHERE dni = :dni")
    Mono<String> findEmailByDni(String dni);

    Mono<UserEntity> findByEmail(String email);
}
