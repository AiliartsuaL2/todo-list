package moais.todolist.global.auth.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUserAccountEvent {
    private String memberId;
}
