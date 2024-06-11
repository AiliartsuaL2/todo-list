package moais.todolist.global.auth.presentation.dto.request;

import lombok.Getter;
import moais.todolist.member.domain.RoleType;

@Getter
public class CreateUserAccountEvent {
    private final String memberId;
    private final String role;

    public CreateUserAccountEvent(String memberId) {
        this.memberId = memberId;
        this.role = RoleType.ROLE_USER.getAuthority();
    }
}

