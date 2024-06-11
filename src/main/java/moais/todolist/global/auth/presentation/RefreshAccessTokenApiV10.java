package moais.todolist.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.auth.application.usecase.RenewAccessTokenUseCase;
import moais.todolist.global.auth.presentation.dto.response.RefreshAccessTokenResponseDto;
import moais.todolist.global.dto.ApiCommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class RefreshAccessTokenApiV10 {

    private final RenewAccessTokenUseCase renewAccessTokenUseCase;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiCommonResponse<RefreshAccessTokenResponseDto>> renewAccessToken(
            @RequestHeader(value = "Authorization")
            String refreshToken
    ) {
        String accessToken = renewAccessTokenUseCase.renewAccessToken(refreshToken);
        return ResponseEntity.ok(new ApiCommonResponse<>(new RefreshAccessTokenResponseDto(accessToken), true));
    }
}
