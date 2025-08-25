package co.juan.crediya.usecase.user;

import co.juan.crediya.model.user.User;
import co.juan.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        if (!Utils.validateField(user.getName())
                || !Utils.validateField(user.getLastName())
                || !Utils.validateEmail(user.getEmail())
                || !Utils.validateMoney(user.getBaseSalary())) {
            throw new IllegalArgumentException("Invalid user credentials");
        }

        return existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    }

                    return userRepository.save(user);
                });
    }

    public final Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
