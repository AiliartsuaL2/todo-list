package moais.todolist.todo.domain;

import moais.todolist.global.exception.PermissionDeniedException;
import moais.todolist.todo.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TodoTest {

    private static final String CONTENT = "content";
    private static final String MEMBER_ID = "memberId";

    @Nested
    @DisplayName("생성 테스트")
    class Create {
        @Test
        @DisplayName("필수 인자 - 내용이 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            String content = null;
            String emptyContent = "";

            // when & then
            Assertions.assertThatThrownBy(() -> new Todo(content, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_CONTENT.getMessage());

            Assertions.assertThatThrownBy(() -> new Todo(emptyContent, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_CONTENT.getMessage());
        }

        @Test
        @DisplayName("필수 인자 - 사용자 ID가 존재하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> new Todo(CONTENT, memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> new Todo(CONTENT, emptyMemberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }


        @Test
        @DisplayName("내용의 입력이 최대 길이가 넘어가는 경우 예외가 발생한다.")
        void test3() {
            // given
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 300; i++) {
                stringBuilder.append(".");
            }
            String content = stringBuilder.toString();

            // when & then
            Assertions.assertThatThrownBy(() -> new Todo(content, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.OVER_THAN_MAX_SIZE_AT_CONTENT.getMessage());
        }
    }

    @Nested
    @DisplayName("상태 수정 테스트")
    class UpdateStatus {

        Todo TODO;
        Todo DONE_TODO;
        Todo IN_PROGRESS_TODO;
        Todo PENDING_TODO;

        @BeforeEach
        void init() {
            TODO = new Todo(CONTENT, MEMBER_ID);
            IN_PROGRESS_TODO = new Todo(CONTENT, MEMBER_ID);
            PENDING_TODO = new Todo(CONTENT, MEMBER_ID);
            DONE_TODO = new Todo(CONTENT, MEMBER_ID);

            IN_PROGRESS_TODO.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);
            DONE_TODO.updateStatus(TodoStatus.DONE.getValue(), MEMBER_ID);
            PENDING_TODO.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);
            PENDING_TODO.updateStatus(TodoStatus.PENDING.getValue(), MEMBER_ID);
        }

        @Test
        @DisplayName("필수 인자 - 변경할 상태가 존재하지 않는 경우 예외가 발생한다.")
        void test1() {
            // given
            String status = null;
            String emptyStatus = "";
            Todo todo = new Todo(CONTENT, MEMBER_ID);

            // when & then
            Assertions.assertThatThrownBy(() -> todo.updateStatus(status, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());

            Assertions.assertThatThrownBy(() -> todo.updateStatus(emptyStatus, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());
        }

        @Test
        @DisplayName("할 일의 MEMBER_ID와 변경자의 ID가 일치하지 않는 경우 예외가 발생한다.")
        void test2() {
            // given
            String invalidMemberId = MEMBER_ID + "invalid";
            Todo todo = new Todo(CONTENT, MEMBER_ID);

            // when & then
            Assertions.assertThatThrownBy(() -> todo.updateStatus(TodoStatus.DONE.getValue(), invalidMemberId))
                    .isInstanceOf(PermissionDeniedException.class)
                    .hasMessage(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }

        @Test
        @DisplayName("정의되지 않은 상태로 변경 요청을 하는 경우 예외가 발생한다.")
        void test3() {
            // given
            String invalidStatus = TodoStatus.TODO.getValue() + "invalid";
            Todo todo = new Todo(CONTENT, MEMBER_ID);

            // when & then
            Assertions.assertThatThrownBy(() -> todo.updateStatus(invalidStatus, MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TODO_STATUS.getMessage());
        }

        @Test
        @DisplayName("정의된 상태에서 변경 할 수 없는 경우 예외가 발생한다. (할 일 -> 대기)")
        void test4() {
            // given
            Todo todo = TODO;

            // when & then
            Assertions.assertThatThrownBy(() -> todo.updateStatus(TodoStatus.PENDING.getValue(), MEMBER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PENDING_IS_ONLY_CHANGE_AT_IN_PROGRESS.getMessage());
        }

        @Test
        @DisplayName("정의된 상태에서 변경 할 수 없는 경우 예외가 발생한다. (완료 -> 대기)")
        void test5() {
            // given
            Todo todo = DONE_TODO;

            // when & then
            Assertions.assertThatThrownBy(() -> todo.updateStatus(TodoStatus.PENDING.getValue(), MEMBER_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(ErrorMessage.PENDING_IS_ONLY_CHANGE_AT_IN_PROGRESS.getMessage());
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (할 일 -> 할 일)")
        void test6() {
            // given
            Todo todo = TODO;

            // when
            todo.updateStatus(TodoStatus.TODO.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (할 일 -> 진행 중)")
        void test7() {
            // given
            Todo todo = TODO;

            // when
            todo.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (할 일 -> 완료)")
        void test8() {
            // given
            Todo todo = TODO;

            // when
            todo.updateStatus(TodoStatus.DONE.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.DONE);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (진행 중 -> 할 일)")
        void test9() {
            // given
            Todo todo = IN_PROGRESS_TODO;

            // when
            todo.updateStatus(TodoStatus.TODO.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (진행 중 -> 진행 중)")
        void test10() {
            // given
            Todo todo = IN_PROGRESS_TODO;

            // when
            todo.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (진행 중 -> 완료)")
        void test11() {
            // given
            Todo todo = IN_PROGRESS_TODO;

            // when
            todo.updateStatus(TodoStatus.DONE.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.DONE);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (진행 중 -> 대기)")
        void test12() {
            // given
            Todo todo = IN_PROGRESS_TODO;

            // when
            todo.updateStatus(TodoStatus.PENDING.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.PENDING);
        }


        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (완료 -> 할 일)")
        void test13() {
            // given
            Todo todo = DONE_TODO;

            // when
            todo.updateStatus(TodoStatus.TODO.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (완료 -> 진행 중)")
        void test14() {
            // given
            Todo todo = DONE_TODO;

            // when
            todo.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("정의된 상태에서는 변경이 가능하다. (완료 -> 완료)")
        void test15() {
            // given
            Todo todo = DONE_TODO;

            // when
            todo.updateStatus(TodoStatus.DONE.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.DONE);
        }

        @Test
        @DisplayName("대기된 상태에서는 어떤 상태로든 변경이 가능하다. (대기 -> 할 일)")
        void test16() {
            // given
            Todo todo = PENDING_TODO;

            // when
            todo.updateStatus(TodoStatus.TODO.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO);
        }

        @Test
        @DisplayName("대기된 상태에서는 어떤 상태로든 변경이 가능하다. (대기 -> 진행 중)")
        void test17() {
            // given
            Todo todo = PENDING_TODO;

            // when
            todo.updateStatus(TodoStatus.IN_PROGRESS.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("대기된 상태에서는 어떤 상태로든 변경이 가능하다. (대기 -> 완료)")
        void test18() {
            // given
            Todo todo = PENDING_TODO;

            // when
            todo.updateStatus(TodoStatus.DONE.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.DONE);
        }

        @Test
        @DisplayName("대기된 상태에서는 어떤 상태로든 변경이 가능하다. (대기 -> 대기)")
        void test19() {
            // given
            Todo todo = PENDING_TODO;

            // when
            todo.updateStatus(TodoStatus.PENDING.getValue(), MEMBER_ID);

            // then
            Assertions.assertThat(todo.getStatus()).isEqualTo(TodoStatus.PENDING);
        }
    }
}
