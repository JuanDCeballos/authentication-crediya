package co.juan.crediya.r2dbc;

import co.juan.crediya.constants.OperationMessages;
import co.juan.crediya.model.dto.LogInDTO;
import co.juan.crediya.model.dto.TokenDTO;
import co.juan.crediya.model.exception.CrediYaException;
import co.juan.crediya.model.exception.ErrorCode;
import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.gateways.UserRepository;
import co.juan.crediya.r2dbc.entity.UserEntity;
import co.juan.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.juan.crediya.security.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements UserRepository {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public Mono<User> saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public Mono<String> findEmailByDni(String dni) {
        return repository.findEmailByDni(dni);
    }

    @Override
    public Mono<TokenDTO> login(LogInDTO logInDTO) {
        return repository.findByEmail(logInDTO.email())
                .doOnNext(request -> log.info(OperationMessages.REQUEST_RECEIVED.getMessage(), logInDTO.toString()))
                .filter(user -> passwordEncoder.matches(logInDTO.password(), user.getPassword()))
                .map(user -> new TokenDTO(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.BAD_CREDENTIALS)));
    }
}
