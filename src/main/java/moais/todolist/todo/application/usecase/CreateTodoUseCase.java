package moais.todolist.todo.application.usecase;

import moais.todolist.todo.application.dto.request.CreateTodoRequestDto;

public interface CreateTodoUseCase {

    void create(String memberId, CreateTodoRequestDto requestDto);
}
