package co.juan.crediya.r2dbc;

import co.juan.crediya.model.dto.LogInDTO;
import co.juan.crediya.model.dto.TokenDTO;
import co.juan.crediya.model.user.User;
import co.juan.crediya.r2dbc.entity.UserEntity;
import co.juan.crediya.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ObjectMapper mapper;

    @Mock
    JwtProvider jwtProvider;

    private User user;
    private UserEntity userEntity;

    private final LogInDTO logInDTO = new LogInDTO("juan.ceballos@correo.com", "123");
    private final TokenDTO tokenDTO = new TokenDTO("eyJh123");

    @BeforeEach
    void initMocks() {
        userEntity = new UserEntity();
        userEntity.setName("Juan");
        userEntity.setLastName("Ceballos");
        userEntity.setEmail("juan.ceballos@correo.com");
        userEntity.setAddress("CRA 97 AA #55-33");
        userEntity.setBaseSalary(BigDecimal.TEN);
        userEntity.setPassword("123");

        user = new User();
        user.setName("Juan");
        user.setLastName("Ceballos");
        user.setEmail("juan.ceballos@correo.com");
        user.setAddress("CRA 97 AA #55-33");
        user.setBaseSalary(BigDecimal.TEN);
        user.setPassword("123");

    }

    @Test
    void mustFindValueById() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Long id = 1L;
        Mono<User> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAllUsers();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(mapper.map(any(User.class), eq(UserEntity.class))).thenReturn(userEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.saveUser(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustExistsByEmail() {
        String email = user.getEmail();

        when(repository.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existsByEmail(email);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(true))
                .verifyComplete();
    }

    @Test
    void findUserByDni() {
        String dni = "1234";

        when(repository.findUserByDni(anyString())).thenReturn(Mono.just(user));

        Mono<User> result = repositoryAdapter.findUserByDni(dni);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void findUserByEmail() {
        String email = "juan@juan.com";

        when(repository.findUserByEmail(anyString())).thenReturn(Mono.just(user));

        Mono<User> result = repositoryAdapter.findUserByEmail(email);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void login() {
        when(repository.findByEmail(anyString())).thenReturn(Mono.just(userEntity));
        when(passwordEncoder.matches(eq(logInDTO.password()), eq(userEntity.getPassword()))).thenReturn(true);
        when(jwtProvider.generateToken(any(UserDetails.class))).thenReturn("eyJh123");

        Mono<TokenDTO> result = repositoryAdapter.login(logInDTO);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(tokenDTO))
                .verifyComplete();
    }
}
