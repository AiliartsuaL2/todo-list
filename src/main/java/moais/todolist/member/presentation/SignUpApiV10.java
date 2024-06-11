package moais.todolist.member.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.dto.ResponseMessage;
import moais.todolist.member.application.dto.request.SignUpRequestDto;
import moais.todolist.member.application.usecase.SignUpUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class SignUpApiV10 {

    private static final String SUCCESS_MESSAGE = "회원 가입에 성공하였습니다.";

    private final SignUpUseCase signUpUseCase;

    @PostMapping("/members/sign-up")
    public ResponseEntity<ApiCommonResponse<ResponseMessage>> signUp(@RequestBody SignUpRequestDto requestDto) {
        signUpUseCase.signUp(requestDto);
        ResponseMessage message = new ResponseMessage(SUCCESS_MESSAGE);
        ApiCommonResponse<ResponseMessage> response = new ApiCommonResponse<>(true, message);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
