package co.juan.crediya.usecase.user;

import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.exception.CrediYaException;
import co.juan.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    UserRepository userRepository;

    private User user;

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
    void saveUser() {
        when(userRepository.saveUser(any(User.class))).thenReturn(Mono.just(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(false));

        Mono<User> response = userUseCase.saveUser(user);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();

        verify(userRepository, times(1)).saveUser(any(User.class));
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void whenUserAlreadyExistByEmail_shouldReturnException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(true));

        Executable executable = () -> {
            Mono<User> mono = userUseCase.saveUser(user);
            mono.block();
        };

        CrediYaException exception = assertThrows(CrediYaException.class, executable);
        assertEquals("Email already exist.", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(0)).saveUser(any(User.class));
    }

    @Test
    void whenBaseSalaryIsNull_shouldReturnException() {
        user.setBaseSalary(null);

        assertThrows(CrediYaException.class, () -> userUseCase.saveUser(user));

        verify(userRepository, times(0)).saveUser(any(User.class));
        verify(userRepository, times(0)).existsByEmail(anyString());
    }

    @Test
    void whenBaseSalaryIsLessThanZero_shouldReturnException() {
        user.setBaseSalary(new BigDecimal("-1"));

        assertThrows(CrediYaException.class, () -> userUseCase.saveUser(user));

        verify(userRepository, times(0)).saveUser(any(User.class));
        verify(userRepository, times(0)).existsByEmail(anyString());
    }

    @Test
    void whenBaseSalaryIsGreaterThanZero_shouldReturnException() {
        user.setBaseSalary(new BigDecimal("1500000000"));

        assertThrows(CrediYaException.class, () -> userUseCase.saveUser(user));

        verify(userRepository, times(0)).saveUser(any(User.class));
        verify(userRepository, times(0)).existsByEmail(anyString());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAllUsers()).thenReturn(Flux.just(user));

        Flux<User> response = userUseCase.getAllUsers();

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();

        verify(userRepository, times(1)).findAllUsers();
    }
}
