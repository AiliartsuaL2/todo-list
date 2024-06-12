package moais.todolist.todo.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.dto.ResponseMessage;
import moais.todolist.todo.application.dto.request.CreateTodoRequestDto;
import moais.todolist.todo.application.usecase.CreateTodoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class CreateTodoApiV10 {

    private static final String SUCCESS_MESSAGE = "할 일이 정상적으로 등록되었습니다.";

    private final CreateTodoUseCase createTodoUseCase;

    @PostMapping("/todos")
    public ResponseEntity<ApiCommonResponse<ResponseMessage>> create(
            @AuthenticationPrincipal
            UserAccount userAccount,
            @RequestBody
            CreateTodoRequestDto requestDto
    ) {
        createTodoUseCase.create(userAccount.getMemberId(), requestDto);
        ApiCommonResponse<ResponseMessage> response = new ApiCommonResponse<>(true, new ResponseMessage(SUCCESS_MESSAGE));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
