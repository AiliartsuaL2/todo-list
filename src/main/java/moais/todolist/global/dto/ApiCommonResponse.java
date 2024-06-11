package moais.todolist.global.dto;

public record ApiCommonResponse<T>(T result, boolean success) {
}

