package moais.todolist.todo.application;

import moais.todolist.global.auth.domain.UserAccount;
import moais.todolist.global.auth.persistence.UserAccountRepository;
import moais.todolist.todo.application.dto.request.CreateTodoRequestDto;
import moais.todolist.todo.application.dto.request.UpdateTodoRequestDto;
import moais.todolist.todo.application.dto.response.GetTodoListResponseDto;
import moais.todolist.todo.application.dto.response.GetTodoResponseDto;
import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.domain.TodoStatus;
import moais.todolist.todo.exception.ErrorMessage;
import moais.todolist.todo.persistence.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    private static final String MEMBER_ID = "memberId";

    private static final String TODO_ID = "todoId";

    private static final String CONTENT = "content";

    private static final String STATUS = TodoStatus.IN_PROGRESS.getValue();

    private final TodoRepository todoRepository = mock(TodoRepository.class);

    private final UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

    private final TodoService todoService = new TodoService(todoRepository, userAccountRepository);

    @Nested
    @DisplayName("TODO 생성 테스트")
    class Create {

        CreateTodoRequestDto requestDto = new CreateTodoRequestDto(CONTENT);

        @Test
        @DisplayName("필수 인자 - 회원 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.create(memberId, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> todoService.create(emptyMemberId, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 회원인 경우, 예외가 발생한다.")
        void test2() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.create(MEMBER_ID, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_BY_ID.getMessage());
        }

        @Test
        @DisplayName("정상 생성시 todo가 저장된다.")
        void test3() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));

            // when
            todoService.create(MEMBER_ID, requestDto);

            // then
            then(todoRepository)
                    .should(times(1))
                    .save(any(Todo.class));
        }
    }

    @Nested
    @DisplayName("최근 TODO 조회 테스트")
    class GetRecentTodo {

        @Test
        @DisplayName("필수 인자 - 회원 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.getRecentTodo(memberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> todoService.getRecentTodo(emptyMemberId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 회원인 경우, 예외가 발생한다.")
        void test2() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.getRecentTodo(MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_BY_ID.getMessage());
        }

        @Test
        @DisplayName("회원 ID에 해당하는 TODO가 존재하지 않는 경우 예외가 발생한다.")
        void test3() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));
            when(todoRepository.findRecentTodo(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.getRecentTodo(MEMBER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TODO.getMessage());
        }

        @Test
        @DisplayName("TODO가 존재하는 경우 ResponseDto로 변환된다.")
        void test4() {
            // given
            Todo todo = mock(Todo.class);
            when(todo.getId())
                    .thenReturn(TODO_ID);
            when(todo.getContent())
                    .thenReturn(CONTENT);
            when(todo.getStatus())
                    .thenReturn(TodoStatus.TODO);
            when(todo.getCreatedAt())
                    .thenReturn(LocalDateTime.now());
            when(todo.getModifiedAt())
                    .thenReturn(LocalDateTime.now());

            when(todoRepository.findRecentTodo(MEMBER_ID))
                    .thenReturn(Optional.of(todo));
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));

            // when
            GetTodoResponseDto recentTodo = todoService.getRecentTodo(MEMBER_ID);

            // then
            assertThat(recentTodo.todoId())
                    .isEqualTo(TODO_ID);
            assertThat(recentTodo.content())
                    .isEqualTo(CONTENT);
            assertThat(recentTodo.status())
                    .isEqualTo(TodoStatus.TODO.name());
            assertThat(recentTodo.createdAt())
                    .isNotNull();
            assertThat(recentTodo.updatedAt())
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("TODO 목록 조회 테스트")
    class GetTodoList {

        @Test
        @DisplayName("필수 인자 - 회원 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.getTodoList(memberId, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> todoService.getTodoList(emptyMemberId, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 회원인 경우, 예외가 발생한다.")
        void test2() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.getTodoList(MEMBER_ID, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_BY_ID.getMessage());
        }

        @Test
        @DisplayName("데이터 미존재시 빈 리스트가 응답된다.")
        void test3() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));
            when(todoRepository.findTodosByMemberId(MEMBER_ID, 10, 0))
                    .thenReturn(new ArrayList<>());
            when(todoRepository.findTodoCountByMemberId(MEMBER_ID))
                    .thenReturn(0L);

            // when
            GetTodoListResponseDto todoList = todoService.getTodoList(MEMBER_ID, 1);

            // then
            assertThat(todoList.count()).isEqualTo(0);
            assertThat(todoList.todos().size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("TODO 상태 수정 테스트")
    class UpdateStatus {

        UpdateTodoRequestDto requestDto = new UpdateTodoRequestDto(STATUS);

        @Test
        @DisplayName("필수 인자 - 회원 ID 미존재시 예외가 발생한다.")
        void test1() {
            // given
            String memberId = null;
            String emptyMemberId = "";

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.updateStatus(memberId, TODO_ID, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

            Assertions.assertThatThrownBy(() -> todoService.updateStatus(emptyMemberId, TODO_ID, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 회원인 경우, 예외가 발생한다.")
        void test2() {
            // given
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.updateStatus(MEMBER_ID, TODO_ID, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_MEMBER_BY_ID.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 TODO ID인 경우 예외가 발생한다.")
        void test3() {
            // given
            when(todoRepository.findById(TODO_ID))
                    .thenReturn(Optional.empty());
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));

            // when & then
            Assertions.assertThatThrownBy(() -> todoService.updateStatus(MEMBER_ID, TODO_ID, requestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ErrorMessage.NOT_EXIST_TODO_BY_ID.getMessage());
        }

        @Test
        @DisplayName("정상 요청인 경우, TODO가 수정된다.")
        void test4() {
            // given
            Todo todo = mock(Todo.class);
            when(todoRepository.findById(TODO_ID))
                    .thenReturn(Optional.of(todo));
            when(userAccountRepository.findUserAccountByMemberId(MEMBER_ID))
                    .thenReturn(Optional.of(mock(UserAccount.class)));

            // when
            todoService.updateStatus(MEMBER_ID, TODO_ID, requestDto);

            // then
            then(todo)
                    .should(times(1))
                    .updateStatus(STATUS, MEMBER_ID);
        }
    }
}