package moais.todolist.todo.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.todo.application.dto.response.GetTodoResponseDto;
import moais.todolist.todo.application.usecase.GetRecentTodoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class GetRecentTodoApiV10 {

    private final GetRecentTodoUseCase getRecentTodoUseCase;

    @GetMapping("/todos/recent")
    public ResponseEntity<ApiCommonResponse<GetTodoResponseDto>> getRecentTodo(
            @AuthenticationPrincipal
            UserAccount userAccount
    ) {
        GetTodoResponseDto recentTodo = getRecentTodoUseCase.getRecentTodo(userAccount.getMemberId());
        ApiCommonResponse<GetTodoResponseDto> response = new ApiCommonResponse<>(true, recentTodo);
        return ResponseEntity.ok(response);
    }
}
