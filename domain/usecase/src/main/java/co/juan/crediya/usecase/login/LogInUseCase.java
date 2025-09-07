package co.juan.crediya.usecase.login;

import co.juan.crediya.model.dto.LogInDTO;
import co.juan.crediya.model.dto.TokenDTO;
import co.juan.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogInUseCase {

    private final UserRepository userRepository;

    public Mono<TokenDTO> login(LogInDTO dto) {
        return userRepository.login(dto);
    }
}
