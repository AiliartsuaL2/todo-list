package moais.todolist.global.auth.application.provider;

public interface CreateTokenProvider {
    String createAccessToken(final String payload);
    String createRefreshToken(final String payload);
}
