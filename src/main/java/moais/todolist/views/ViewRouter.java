package moais.todolist.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views")
public class ViewRouter {

    @GetMapping("/main")
    public String main() {
        return "/views/index.html";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "/views/sign-up.html";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "/views/sign-in.html";
    }

    @GetMapping("/todos")
    public String todos() {
        return "/views/todos.html";
    }
}
