package moais.todolist.member.presentation;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.dto.ApiCommonResponse;
import moais.todolist.global.dto.ResponseMessage;
import moais.todolist.member.application.dto.request.WithdrawRequestDto;
import moais.todolist.member.application.usecase.WithdrawUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v10")
public class WithdrawApiV10 {

    private static final String SUCCESS_MESSAGE = "회원 탈퇴가 정상적으로 처리되었습니다.";

    private final WithdrawUseCase withdrawUseCase;

    @DeleteMapping("/members")
    public ResponseEntity<ApiCommonResponse<ResponseMessage>> withdraw(
            @RequestBody
            WithdrawRequestDto requestDto,
            @AuthenticationPrincipal
            UserAccount userAccount
            ) {
        withdrawUseCase.withdraw(requestDto, userAccount.getMemberId());
        ResponseMessage message = new ResponseMessage(SUCCESS_MESSAGE);
        ApiCommonResponse<ResponseMessage> response = new ApiCommonResponse<>(true, message);
        return ResponseEntity.ok(response);
    }
}
