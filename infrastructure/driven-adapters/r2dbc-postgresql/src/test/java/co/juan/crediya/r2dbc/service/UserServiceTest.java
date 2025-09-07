package co.juan.crediya.r2dbc.service;

import co.juan.crediya.model.user.User;
import co.juan.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    TransactionalOperator transactionalOperator;

    @Mock
    UserUseCase userUseCase;

    private User user;
    private final String email = "example@gmail.com";
    private final String dni = "1234";

    @BeforeEach
    void initMocks() {
        user = new User();
        user.setName("Juan");
        user.setLastName("Ceballos");
        user.setBirthDate(LocalDateTime.of(2025, 8, 25, 20, 46));
        user.setEmail("juan.ceballos@correo.com");
        user.setAddress("CRA 97 AA #55-33");
        user.setBaseSalary(BigDecimal.TEN);
    }

    @Test
    void saveUser_shouldSaveUserSuccessfully() {
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(user));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<User>) callback.doInTransaction(null)).flux();
                });

        Mono<User> result = userService.saveUser(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(userUseCase).saveUser(user);
        verify(transactionalOperator).execute(any());
    }

    @Test
    void saveUser_shouldHandleError() {
        RuntimeException exception = new RuntimeException("Error al guardar");

        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.error(exception));

        when(transactionalOperator.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return ((Mono<User>) callback.doInTransaction(null)).flux();
                });

        Mono<User> result = userService.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error al guardar"))
                .verify();

        verify(userUseCase).saveUser(user);
        verify(transactionalOperator).execute(any());
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user1 = User.builder().name("Juan").build();
        User user2 = User.builder().name("Pedro").build();

        when(userUseCase.getAllUsers()).thenReturn(Flux.just(user1, user2));

        Flux<User> result = userService.getAllUsers();

        StepVerifier.create(result)
                .expectNext(user1)
                .expectNext(user2)
                .verifyComplete();

        verify(userUseCase).getAllUsers();
    }

    @Test
    void findUserByDni() {
        when(userUseCase.findUserByDni(anyString())).thenReturn(Mono.just(user));

        Mono<User> result = userService.findUserByDni(dni);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(userUseCase).findUserByDni(anyString());
    }

    @Test
    void findUserByEmail() {
        when(userUseCase.findUserByEmail(anyString())).thenReturn(Mono.just(user));

        Mono<User> result = userService.findUserByEmail(email);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(userUseCase).findUserByEmail(anyString());
    }
}
