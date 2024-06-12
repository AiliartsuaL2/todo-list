package moais.todolist.todo.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import moais.todolist.todo.domain.QTodo;
import moais.todolist.todo.domain.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static moais.todolist.todo.domain.QTodo.todo;


@RequiredArgsConstructor
@Repository
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findRecentTodo(String memberId) {
        Todo latestTodo = queryFactory
                .selectFrom(todo)
                .orderBy(todo.modifiedAt.coalesce(todo.createdAt).desc())
                .where(todo.memberId.eq(memberId))
                .fetchFirst();
        return Optional.ofNullable(latestTodo);
    }

    @Override
    public List<Todo> findTodosByMemberId(String memberId, int limit, int offset) {
        return queryFactory.selectFrom(todo)
                .where(todo.memberId.eq(memberId))
                .orderBy(todo.modifiedAt.coalesce(todo.createdAt).desc())
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public Long findTodoCountByMemberId(String memberId) {
        return queryFactory.select(todo.count())
                .from(todo)
                .where(todo.memberId.eq(memberId))
                .fetchOne();
    }
}
