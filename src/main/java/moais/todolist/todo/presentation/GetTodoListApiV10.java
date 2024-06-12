package moais.todolist.todo.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.todo.application.dto.response.GetTodoListResponseDto;
import moais.todolist.todo.application.usecase.GetTodoListUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class GetTodoListApiV10 {

    private final GetTodoListUseCase getTodoListUseCase;

    @GetMapping("/todos")
    public ResponseEntity<ApiCommonResponse<GetTodoListResponseDto>> getTodoList(
            @AuthenticationPrincipal
            UserAccount userAccount,
            @RequestParam
            Integer page
    ) {
        GetTodoListResponseDto todoList = getTodoListUseCase.getTodoList(userAccount.getMemberId(), page);
        ApiCommonResponse<GetTodoListResponseDto> response = new ApiCommonResponse<>(true, todoList);
        return ResponseEntity.ok(response);
    }
}
