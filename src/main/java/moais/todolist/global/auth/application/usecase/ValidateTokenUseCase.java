package moais.todolist.global.auth.application.usecase;

public interface ValidateTokenUseCase {
    boolean isValid(String token);
}
