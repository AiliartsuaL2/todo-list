package moais.todolist.todo.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moais.todolist.global.annotation.Event;
import moais.todolist.global.domain.event.DeleteMemberEvent;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.global.exception.EventException;
import moais.todolist.todo.application.usecase.DeleteTodoUseCase;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.ObjectUtils;

@Slf4j
@Event
@RequiredArgsConstructor
public class DeleteTodosEventListener {

    private final DeleteTodoUseCase deleteTodoUseCase;

    @TransactionalEventListener(value = DeleteMemberEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void deleteTodo(DeleteMemberEvent event) {
        validateMemberIdAtEvent(event.getMemberId());
        deleteTodoUseCase.deleteByMemberId(event.getMemberId());
    }

    private void validateMemberIdAtEvent(String memberId) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new EventException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
    }
}
