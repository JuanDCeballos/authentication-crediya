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
        if (!Utils.validateMoney(user.getBaseSalary())) {
            throw new IllegalArgumentException("Base salary isn't within range");
        }

        return userRepository.existsByEmail(user.getEmail())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email already exists")))
                .then(userRepository.saveUser(user));
    }

    public final Flux<User> getAllUsers() {
        return userRepository.findAllUsers();
    }
}
