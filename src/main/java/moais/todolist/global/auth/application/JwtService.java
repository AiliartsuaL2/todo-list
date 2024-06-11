package moais.todolist.global.auth.application;

import lombok.RequiredArgsConstructor;
import moais.todolist.global.auth.application.provider.CreateTokenProvider;
import moais.todolist.global.auth.application.provider.GetTokenInfoProvider;
import moais.todolist.global.auth.application.usecase.*;
import moais.todolist.global.auth.domain.Token;
import moais.todolist.global.exception.EmptyTokenException;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class JwtService implements CreateTokenUseCase, ExtractPayloadUseCase, RenewAccessTokenUseCase, ValidateTokenUseCase, GetAuthenticationUseCase {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final CreateTokenProvider createTokenProvider;

    private final GetTokenInfoProvider getTokenInfoProvider;

    private final UserDetailsService userDetailsService;

    @Override
    public Token create(String memberId) {
        // 회원 검증
        userDetailsService.loadUserByUsername(memberId);
        if (ObjectUtils.isEmpty(memberId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        return new Token(createTokenProvider.createAccessToken(memberId), createTokenProvider.createRefreshToken(memberId));
    }

    @Override
    public String extractPayload(String accessToken) {
        validateExistToken(accessToken);
        return getTokenInfoProvider.getPayload(accessToken);
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        validateExistToken(refreshToken);
        String payload = getTokenInfoProvider.getPayload(refreshToken);
        return createTokenProvider.createAccessToken(payload);
    }

    @Override
    public boolean isValid(String token) {
        validateExistToken(token);
        return getTokenInfoProvider.isValid(token);
    }

    @Override
    @Transactional(readOnly = true)
    public Authentication getAuthentication(String accessToken) {
        validateExistToken(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenInfoProvider.getPayload(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private void validateExistToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new EmptyTokenException(ErrorMessage.NOT_EXIST_TOKEN.getMessage());
        }
    }
}
