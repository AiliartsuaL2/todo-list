package moais.todolist.global.auth.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.annotation.Event;
import moais.todolist.global.auth.application.usecase.CreateUserAccountUseCase;
import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.presentation.dto.request.CreateUserAccountEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@Event
@RequiredArgsConstructor
public class CreateUserAccountEventListener {

    private final CreateUserAccountUseCase createUserAccountUseCase;

    @EventListener(CreateUserAccountEvent.class)
    public void createUserAccount(CreateUserAccountEvent event) {
        createUserAccountUseCase.create(new UserAccount(event.getMemberId(), event.getRole()));
    }
}