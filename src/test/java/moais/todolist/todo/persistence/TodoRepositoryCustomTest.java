package moais.todolist.todo.persistence;

import moais.todolist.member.domain.Member;
import moais.todolist.member.persistence.MemberRepository;
import moais.todolist.testconfig.TestQueryDslConfig;
import moais.todolist.todo.domain.Todo;
import moais.todolist.todo.domain.TodoStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestQueryDslConfig.class)
class TodoRepositoryCustomTest {

    private static final String NICKNAME = "nickname";
    private static final String LOGIN_ID = "loginId";
    private static final String PASSWORD = "password123!";
    private static final String CONTENT = "content";


    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("회원의 최근 TODO 조회 테스트")
    class FindRecentTodo {

        String memberId;
        String lastCreatedTodoId;

        @BeforeEach
        void init() {
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            memberId = member.getId();

            // 생성 시각을 18:00:00 ~ 18:50:00 설정, 수정 시각은 null
            for (int i = 0; i < 50; i++) {
                String todoId = UUID.randomUUID().toString();
                String sql = "INSERT INTO TODO (id, member_id, content, status, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(sql, todoId, memberId, CONTENT, TodoStatus.TODO.getPersistValue(),
                        LocalDateTime.of(2024, 6, 1, 18, i, 0),
                        null);

                if (i == 49) {
                    lastCreatedTodoId = todoId;
                }
            }
        }

        @Test
        @DisplayName("수정한 Todo가 없는 경우 가장 최근에 생성된 Todo의 데이터가 조회된다.")
        void test1() {
            // given & when
            Todo todo = todoRepository.findRecentTodo(memberId).get();

            // then
            assertThat(todo.getId()).isEqualTo(lastCreatedTodoId);
        }

        @Test
        @DisplayName("마지막 생성 일자보다 최근에 수정된 Todo의 데이터가 있는 경우, 수정된 TODO가 조회된다.")
        void test2() {
            // given
            String sql = "INSERT INTO TODO (id, member_id, content, status, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
            String todoId = UUID.randomUUID().toString();
            // 1일 12시 생성, 2일 12시 수정 데이터 추가
            jdbcTemplate.update(sql, todoId, memberId, CONTENT, TodoStatus.TODO.getPersistValue(),
                    LocalDateTime.of(2024, 6, 1, 12, 0, 0),
                    LocalDateTime.of(2024, 6, 2, 12, 0, 0));

            // when
            Todo todo = todoRepository.findRecentTodo(memberId).get();

            // then
            assertThat(todo.getId()).isEqualTo(todoId);
        }

        @Test
        @DisplayName("마지막 생성 일자보다 이전에 수정된 Todo의 데이터가 있는 경우, 마지막에 생성된 TODO가 조회된다.")
        void test3() {
            // given
            String sql = "INSERT INTO TODO (id, member_id, content, status, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
            String todoId = UUID.randomUUID().toString();
            // 1일 12시 생성, 1일 18시 45분 (마지막 생성 일자보다 이전) 수정 데이터 추가
            jdbcTemplate.update(sql, todoId, memberId, CONTENT, TodoStatus.TODO.getPersistValue(),
                    LocalDateTime.of(2024, 6, 1, 12, 0, 0),
                    LocalDateTime.of(2024, 6, 1, 18, 45, 0));

            // when
            Todo todo = todoRepository.findRecentTodo(memberId).get();

            // then
            assertThat(todo.getId()).isEqualTo(lastCreatedTodoId);
        }

        @Test
        @DisplayName("마지막 생성일자 이후에 수정된 Todo의 데이터가 여러개 있는 경우, 마지막에 수정된 TODO가 조회된다.")
        void test4() {
            // given
            String sql = "INSERT INTO TODO (id, member_id, content, status, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
            String firstModifiedTodoId = UUID.randomUUID().toString();
            String lastModifiedTodoId = UUID.randomUUID().toString();

            // 1일 12시 생성, 2일 12시 수정 데이터 추가
            jdbcTemplate.update(sql, firstModifiedTodoId, memberId, CONTENT, TodoStatus.TODO.getPersistValue(),
                    LocalDateTime.of(2024, 6, 1, 12, 0, 0),
                    LocalDateTime.of(2024, 6, 2, 12, 0, 0));
            // 1일 12시 생성, 2일 18시 수정 데이터 추가
            jdbcTemplate.update(sql, lastModifiedTodoId, memberId, CONTENT, TodoStatus.TODO.getPersistValue(),
                    LocalDateTime.of(2024, 6, 1, 12, 0, 0),
                    LocalDateTime.of(2024, 6, 2, 18, 0, 0));

            // when
            Todo todo = todoRepository.findRecentTodo(memberId).get();

            // then
            assertThat(todo.getId()).isEqualTo(lastModifiedTodoId);
        }

        @Test
        @DisplayName("생성된 Todo가 없는 경우, Optional.empty()가 반환된다.")
        void test5() {
            // given
            jdbcTemplate.execute("TRUNCATE TABLE TODO");

            // when
            Optional<Todo> todo = todoRepository.findRecentTodo(memberId);

            // then
            assertThat(todo).isEmpty();
        }
    }

    @Nested
    @DisplayName("회원의 TODO 리스트 조회 테스트")
    class FindTodosByMemberId {
        @Test
        @DisplayName("등록된 TODO가 없는 경우, List 사이즈가 0이다.")
        void test1() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);

            // when
            List<Todo> todos = todoRepository.findTodosByMemberId(member.getId(), 10, 0);

            // then
            assertThat(todos.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("등록된 TODO가 limit개 이상인 경우, List 사이즈가 limit개 만큼 데이터가 조회된다.")
        void test2() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            int limit = 20;

            for (int i = 0; i < (limit + 20); i++) {
                Todo todo = new Todo(CONTENT, member.getId());
                todoRepository.save(todo);
            }

            // when
            List<Todo> todos = todoRepository.findTodosByMemberId(member.getId(), limit, 0);

            // then
            assertThat(todos.size()).isEqualTo(limit);
        }

        @Test
        @DisplayName("등록된 TODO가 limit개 미만인 경우, List 사이즈가 등록된 개수 만큼 데이터가 조회된다.")
        void test3() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            int limit = 20;
            int registeredTodoCount = 10;

            for (int i = 0; i < registeredTodoCount; i++) {
                Todo todo = new Todo(CONTENT, member.getId());
                todoRepository.save(todo);
            }

            // when
            List<Todo> todos = todoRepository.findTodosByMemberId(member.getId(), limit, 0);

            // then
            assertThat(todos.size()).isEqualTo(registeredTodoCount);
        }
    }

    @Nested
    @DisplayName("회원의 TODO 개수 조회 테스트")
    class FindTodoCountByMemberId {
        @Test
        @DisplayName("등록을 하지 않은 경우, 개수가 0개 조회된다.")
        void test1() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);

            // when
            Long count = todoRepository.findTodoCountByMemberId(member.getId());

            // then
            assertThat(count).isEqualTo(0);
        }

        @Test
        @DisplayName("해당 회원이 n개를 등록한 경우, 개수가 n개 조회된다.")
        void test2() {
            // given
            Member member = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(member);
            int registeredTodoCount = 100;

            for (int i = 0; i < registeredTodoCount; i++) {
                Todo todo = new Todo(CONTENT, member.getId());
                todoRepository.save(todo);
            }

            // when
            Long count = todoRepository.findTodoCountByMemberId(member.getId());

            // then
            assertThat(count).isEqualTo(registeredTodoCount);
        }

        @Test
        @DisplayName("A 회원이 n개를 등록하고, B 회원이 m개를 등록하면, A 회원 조회시 n개, B 회원 조회시 m개가 조회된다.")
        void test3() {
            // given
            Member memberA = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            Member memberB = new Member(NICKNAME, LOGIN_ID, PASSWORD);
            memberRepository.save(memberA);
            memberRepository.save(memberB);

            int registeredTodoCountA = 100;
            int registeredTodoCountB = 200;

            for (int i = 0; i < registeredTodoCountA; i++) {
                Todo todo = new Todo(CONTENT, memberA.getId());
                todoRepository.save(todo);
            }

            for (int i = 0; i < registeredTodoCountB; i++) {
                Todo todo = new Todo(CONTENT, memberB.getId());
                todoRepository.save(todo);
            }

            // when
            Long countA = todoRepository.findTodoCountByMemberId(memberA.getId());
            Long countB = todoRepository.findTodoCountByMemberId(memberB.getId());

            // then
            assertThat(countA).isEqualTo(registeredTodoCountA);
            assertThat(countB).isEqualTo(registeredTodoCountB);
        }
    }
}