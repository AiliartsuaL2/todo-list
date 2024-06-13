package moais.todolist.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.annotation.Event;
import moais.todolist.global.auth.application.usecase.CreateUserAccountUseCase;
import moais.todolist.global.auth.application.usecase.DeleteUserAccountUseCase;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.domain.event.CreateMemberEvent;
import moais.todolist.global.domain.event.DeleteMemberEvent;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.global.exception.EventException;
import org.springframework.context.event.EventListener;
import org.springframework.util.ObjectUtils;

@Slf4j
@Event
@RequiredArgsConstructor
public class UserAccountEventListener {

    private final CreateUserAccountUseCase createUserAccountUseCase;

    private final DeleteUserAccountUseCase deleteUserAccountUseCase;

    @EventListener(CreateMemberEvent.class)
    public void createUserAccount(CreateMemberEvent event) {
        validateMemberIdAtEvent(event.getMemberId());
        createUserAccountUseCase.create(new UserAccount(event.getMemberId(), event.getRole()));
    }

    @EventListener(DeleteMemberEvent.class)
    public void deleteUserAccount(DeleteMemberEvent event) {
        validateMemberIdAtEvent(event.getMemberId());
        deleteUserAccountUseCase.deleteByMemberId(event.getMemberId());
    }

    private void validateMemberIdAtEvent(String memberId) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new EventException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
    }
}