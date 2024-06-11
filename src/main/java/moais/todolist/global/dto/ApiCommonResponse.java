package moais.todolist.global.dto;

public record ApiCommonResponse<T>(boolean success, T result) {
}

