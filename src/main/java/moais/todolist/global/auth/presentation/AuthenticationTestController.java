package moais.todolist.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationTestController {

    @GetMapping("/api/v1/auth/use/test")
    public String useAuthentication() {
        return "success";
    }

    @GetMapping("/api/v1/auth/not-use/test")
    public String notUseAuthentication() {
        return "success";
    }
}
