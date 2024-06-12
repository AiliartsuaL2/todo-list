package moais.todolist.todo.application.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moais.todolist.todo.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdateTodoRequestDtoTest {
    @Test
    @DisplayName("필수 인자 - 내용이 존재하지 않는 경우 예외가 발생한다.")
    void test1() {
        // given
        String status = null;
        String emptyStatus = "";

        // when & then
        assertThatThrownBy(() -> new UpdateTodoRequestDto(status))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());

        assertThatThrownBy(() -> new UpdateTodoRequestDto(emptyStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());
    }

    @Test
    @DisplayName("정상 생성시 입력 필드가 매핑된다.")
    void test2() {
        // given
        String status = "status";

        // when
        UpdateTodoRequestDto requestDto = new UpdateTodoRequestDto(status);

        // then
        assertThat(requestDto.status()).isEqualTo(status);
    }
}
