package moais.todolist.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MemberInfo {

    private String nickname;

    public MemberInfo(String nickname) {
        this.nickname = nickname;
    }
}
