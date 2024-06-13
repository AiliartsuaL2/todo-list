package moais.todolist.global.domain.event;

import lombok.Getter;
import moais.todolist.member.domain.RoleType;

@Getter
public class CreateMemberEvent {
    private final String memberId;
    private final String role;

    public CreateMemberEvent(String memberId) {
        this.memberId = memberId;
        this.role = RoleType.ROLE_USER.getAuthority();
    }
}

