package moais.todolist.global.auth.application.usecase;
import org.springframework.security.core.Authentication;

public interface GetAuthenticationUseCase {
    Authentication getAuthentication(String accessToken);
}
