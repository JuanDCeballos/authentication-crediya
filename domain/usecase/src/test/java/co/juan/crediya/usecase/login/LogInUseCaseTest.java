package co.juan.crediya.usecase.login;

import co.juan.crediya.model.dto.LogInDTO;
import co.juan.crediya.model.dto.TokenDTO;
import co.juan.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogInUseCaseTest {

    @InjectMocks
    LogInUseCase logInUseCase;

    @Mock
    UserRepository userRepository;

    private final LogInDTO logInDTO = new LogInDTO("myEmail@email.com", "123456789_Ju");
    private final TokenDTO tokenDTO = new TokenDTO("eyJh");

    @Test
    void login() {
        when(userRepository.login(any(LogInDTO.class))).thenReturn(Mono.just(tokenDTO));

        Mono<TokenDTO> response = logInUseCase.login(logInDTO);

        StepVerifier.create(response)
                .expectNextMatches(value -> value.equals(tokenDTO))
                .verifyComplete();

        verify(userRepository, times(1)).login(any(LogInDTO.class));
    }
}
