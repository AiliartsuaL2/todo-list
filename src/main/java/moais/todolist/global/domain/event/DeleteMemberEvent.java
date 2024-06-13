package moais.todolist.global.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteMemberEvent {
    private String memberId;
}
