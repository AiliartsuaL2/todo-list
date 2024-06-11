package moais.todolist.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.auth.application.usecase.RenewAccessTokenUseCase;
import moais.todolist.global.auth.presentation.dto.request.RenewAccessTokenRequestDto;
import moais.todolist.global.auth.presentation.dto.response.RefreshAccessTokenResponseDto;
import moais.todolist.global.dto.ApiCommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v10")
@RequiredArgsConstructor
public class RefreshAccessTokenApiV10 {

    private final RenewAccessTokenUseCase renewAccessTokenUseCase;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiCommonResponse<RefreshAccessTokenResponseDto>> renewAccessToken(@RequestBody RenewAccessTokenRequestDto requestDto) {
        String accessToken = renewAccessTokenUseCase.renewAccessToken(requestDto.refreshToken());
        return ResponseEntity.ok(new ApiCommonResponse<>(new RefreshAccessTokenResponseDto(accessToken), true));
    }
}
