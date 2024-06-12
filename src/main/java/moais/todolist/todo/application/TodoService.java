package moais.todolist.todo.application;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.todo.application.dto.request.CreateTodoRequestDto;
import moais.todolist.todo.application.dto.request.UpdateTodoRequestDto;
import moais.todolist.todo.application.dto.response.GetTodoListResponseDto;
import moais.todolist.todo.application.dto.response.GetTodoResponseDto;
import moais.todolist.todo.application.usecase.CreateTodoUseCase;
import moais.todolist.todo.application.usecase.GetRecentTodoUseCase;
import moais.todolist.todo.application.usecase.GetTodoListUseCase;
import moais.todolist.todo.application.usecase.UpdateTodoStatusUseCase;
import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.exception.ErrorMessage;
import moais.todolist.todo.persistence.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService implements CreateTodoUseCase, GetRecentTodoUseCase, GetTodoListUseCase, UpdateTodoStatusUseCase {

    private static final Integer TODO_LIST_LIMIT = 20;

    private final TodoRepository todoRepository;

    private final UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public void create(String memberId, CreateTodoRequestDto requestDto) {
        validateMember(memberId);
        Todo todo = new Todo(requestDto.content(), memberId);
        todoRepository.save(todo);
    }

    @Override
    public GetTodoResponseDto getRecentTodo(String memberId) {
        validateMember(memberId);
        Todo todo = todoRepository.findRecentTodo(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_TODO.getMessage()));
        return GetTodoResponseDto.of(todo);
    }

    @Override
    public GetTodoListResponseDto getTodoList(String memberId, int page) {
        validateMember(memberId);
        int offset = getOffset(page);
        List<Todo> todos = todoRepository.findTodosByMemberId(memberId, TODO_LIST_LIMIT, offset);
        Long count = todoRepository.findTodoCountByMemberId(memberId);
        List<GetTodoResponseDto> todoList = todos.stream()
                .map(GetTodoResponseDto::of)
                .toList();
        return new GetTodoListResponseDto(count, todoList);
    }

    @Override
    @Transactional
    public void updateStatus(String memberId, String todoId, UpdateTodoRequestDto requestDto) {
        validateMember(memberId);
        Todo todo = findById(todoId);
        todo.updateStatus(requestDto.status(), memberId);
    }

    private Todo findById(String todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_TODO_BY_ID.getMessage()));
    }

    private void validateMember(String memberId) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        userAccountRepository.findUserAccountByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_BY_ID.getMessage()));
    }

    private int getOffset(int page) {
        return TODO_LIST_LIMIT * (page - 1);
    }
}
