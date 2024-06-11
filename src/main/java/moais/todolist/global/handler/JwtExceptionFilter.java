package moais.todolist.global.handler;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.exception.EmptyTokenException;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtErrorResponseHandler jwtErrorResponseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (EmptyTokenException e) {
            // authentication이 존재하지 않을때
            jwtErrorResponseHandler.generateJwtErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, ErrorMessage.INCORRECT_TOKEN_TYPE.getMessage(), HttpStatus.FORBIDDEN);
        } catch (JwtException e) {
            jwtErrorResponseHandler.generateJwtErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}