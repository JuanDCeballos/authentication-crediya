package co.juan.crediya.usecase.user;

import co.juan.crediya.model.user.User;
import co.juan.crediya.model.exception.CrediYaException;
import co.juan.crediya.model.exception.ErrorCode;
import co.juan.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    private static final BigDecimal MIN_BASE_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_BASE_SALARY = new BigDecimal("15000000");

    public Mono<User> saveUser(User user) {
        if (user.getBaseSalary() == null || user.getBaseSalary().compareTo(MIN_BASE_SALARY) < 0
                || user.getBaseSalary().compareTo(MAX_BASE_SALARY) > 0) {
            throw new CrediYaException(ErrorCode.INVALID_BASE_SALARY);
        }

        Mono<Boolean> emailExists = userRepository.existsByEmail(user.getEmail());
        Mono<Boolean> dniExists = userRepository.findUserByDni(user.getDni())
                .hasElement();

        return Mono.zip(emailExists, dniExists)
                .filter(tuple -> !tuple.getT1())
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.EMAIL_ALREADY_EXISTS)))
                .filter(tuple -> !tuple.getT2())
                .switchIfEmpty(Mono.error(new CrediYaException(ErrorCode.DNI_ALREADY_EXISTS)))
                .flatMap(result ->
                        userRepository.saveUser(user)
                );
    }

    public final Flux<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<User> findUserByDni(String dni) {
        return userRepository.findUserByDni(dni);
    }

    public Mono<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
