package moais.todolist.global.auth.application.usecase;

public interface ExtractPayloadUseCase {
    String extractPayload(String accessToken);
}
