package co.juan.crediya.r2dbc;

import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.gateways.UserRepository;
import co.juan.crediya.r2dbc.entity.UserEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
        > implements UserRepository {
    public UserReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return save(user);
    }

    @Override
    public Flux<User> findAllUsers() {
        return findAll();
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
