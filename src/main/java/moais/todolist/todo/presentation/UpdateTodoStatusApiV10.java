package moais.todolist.todo.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.dto.ResponseMessage;
import moais.todolist.todo.application.dto.request.UpdateTodoRequestDto;
import moais.todolist.todo.application.usecase.UpdateTodoStatusUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class UpdateTodoStatusApiV10 {

    private static final String SUCCESS_MESSAGE = "할 일의 상태가 정상적으로 변경되었습니다.";

    private final UpdateTodoStatusUseCase updateTodoStatusUseCase;

    @PatchMapping("/todos/{todoId}")
    public ResponseEntity<ApiCommonResponse<ResponseMessage>> updateTodoStatus(
            @AuthenticationPrincipal
            UserAccount userAccount,
            @PathVariable
            String todoId,
            @RequestBody
            UpdateTodoRequestDto requestDto
    ) {
        updateTodoStatusUseCase.updateStatus(userAccount.getMemberId(), todoId, requestDto);
        ApiCommonResponse<ResponseMessage> response = new ApiCommonResponse<>(true, new ResponseMessage(SUCCESS_MESSAGE));
        return ResponseEntity.ok(response);
    }
}
