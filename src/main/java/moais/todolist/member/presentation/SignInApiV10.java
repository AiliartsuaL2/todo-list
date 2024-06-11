package moais.todolist.member.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.member.application.dto.request.SignInRequestDto;
import moais.todolist.member.application.dto.response.SignInResponseDto;
import moais.todolist.member.application.usecase.SignInUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class SignInApiV10 {

    private final SignInUseCase signInUseCase;

    @PostMapping("/members/sign-in")
    public ResponseEntity<ApiCommonResponse<SignInResponseDto>> signIn(@RequestBody SignInRequestDto requestDto) {
        SignInResponseDto responseDto = signInUseCase.signIn(requestDto);
        ApiCommonResponse<SignInResponseDto> response = new ApiCommonResponse<>(true, responseDto);
        return ResponseEntity.ok(response);
    }
}
