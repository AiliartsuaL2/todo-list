package moais.todolist.todo.persistence;

import moais.todolist.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, String>, TodoRepositoryCustom {
    void deleteAllByMemberId(String memberId);
}
