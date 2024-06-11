package moais.todolist.global.auth.application.provider;

public interface GetTokenInfoProvider {
    String getPayload(String token);
    boolean isValid(String token);
}
