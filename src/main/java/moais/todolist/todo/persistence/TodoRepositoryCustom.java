package moais.todolist.todo.persistence;

import moais.todolist.todo.domain.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepositoryCustom {

    Optional<Todo> findRecentTodo(String memberId);

    List<Todo> findTodosByMemberId(String memberId, int limit, int offset);

    Long findTodoCountByMemberId(String memberId);
}
