package moais.todolist.global.auth.application.usecase;

public interface RenewAccessTokenUseCase {
    String renewAccessToken(String refreshToken);
}
