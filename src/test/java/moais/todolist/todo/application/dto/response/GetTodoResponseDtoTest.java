package moais.todolist.todo.application.dto.response;

import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.domain.TodoStatus;
import moais.todolist.todo.utils.DateTimeFormatUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTodoResponseDtoTest {
    @Test
    @DisplayName("팩토리 메서드 실행시, Todo의 필드들이 매핑되고, 날짜는 변환된다.")
    void test1() {
        // given
        Todo todo = mock(Todo.class);
        String todoId = UUID.randomUUID().toString();
        String content = "content";
        String status = TodoStatus.IN_PROGRESS.getName();
        LocalDateTime now =  LocalDateTime.now();
        String nowToStr = DateTimeFormatUtil.toString(now);

        when(todo.getId())
                .thenReturn(todoId);
        when(todo.getContent())
                .thenReturn(content);
        when(todo.getStatus())
                .thenReturn(TodoStatus.IN_PROGRESS);
        when(todo.getCreatedAt())
                .thenReturn(now);
        when(todo.getModifiedAt())
                .thenReturn(now);

        // when
        GetTodoResponseDto responseDto = GetTodoResponseDto.of(todo);

        // then
        Assertions.assertThat(responseDto.todoId()).isEqualTo(todoId);
        Assertions.assertThat(responseDto.content()).isEqualTo(content);
        Assertions.assertThat(responseDto.status()).isEqualTo(status);
        Assertions.assertThat(responseDto.createdAt()).isEqualTo(nowToStr);
        Assertions.assertThat(responseDto.updatedAt()).isEqualTo(nowToStr);
    }
}