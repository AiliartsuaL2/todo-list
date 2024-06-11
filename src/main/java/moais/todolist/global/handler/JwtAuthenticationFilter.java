package moais.todolist.global.handler;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.auth.application.JwtService;
import moais.todolist.global.auth.application.usecase.GetAuthenticationUseCase;
import moais.todolist.global.auth.application.usecase.ValidateTokenUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달.
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final GetAuthenticationUseCase getAuthenticationUseCase;

    private final ValidateTokenUseCase validateTokenUseCase;

    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.getAuthenticationUseCase = jwtService;
        this.validateTokenUseCase = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, JwtException {
        // 헤더에서 JWT 를 받아옴
        String token = resolveToken((HttpServletRequest) request);

        //Bearer 토큰인지 확인
        if (token != null && token.startsWith(TOKEN_PREFIX)){
            token = token.replace(TOKEN_PREFIX,"");
            // token validation
            if (validateTokenUseCase.isValid(token)) {
                Authentication authentication = getAuthenticationUseCase.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}