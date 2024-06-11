package moais.todolist.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moais.todolist.global.domain.BaseEntity;
import moais.todolist.global.exception.PermissionDeniedException;
import moais.todolist.todo.exception.ErrorMessage;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TODO")
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false, length = 20)
    private TodoStatus status;

    @Column(nullable = false, length = 36)
    private String memberId;

    @Transient
    private MemberInfo memberInfo;

    public Todo(String content, String memberId) {
        validateContent(content);
        notNullValidation(memberId, ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());

        this.content = content;
        this.status = TodoStatus.TODO;
        this.memberId = memberId;
    }

    public void updateStatus(String status, String memberId) {
        checkTodoOwner(memberId);
        TodoStatus newStatus = TodoStatus.findByValue(status);

        // 대기 상태이면 어떤 상태로든 변경이 가능
        if (TodoStatus.PENDING.equals(this.status)) {
            this.status = newStatus;
            return;
        }

        // 그 외의 상태의 경우 진행 중 상태에서만 변경이 가능하다.
        validateChangeToPending(newStatus);
        this.status = newStatus;
    }

    private void validateChangeToPending(TodoStatus newStatus) {
        if (TodoStatus.PENDING.equals(newStatus)) {
            if (!this.status.equals(TodoStatus.IN_PROGRESS)) {
                throw new IllegalStateException(ErrorMessage.PENDING_IS_ONLY_CHANGE_AT_IN_PROGRESS.getMessage());
            }
        }
    }

    private void validateContent(String content) {
        notNullValidation(content, ErrorMessage.NOT_EXIST_CONTENT.getMessage());
        if (content.length() > 255) {
            throw new IllegalArgumentException(ErrorMessage.OVER_THAN_MAX_SIZE_AT_CONTENT.getMessage());
        }
    }

    private void checkTodoOwner(String memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new PermissionDeniedException(ErrorMessage.PERMISSION_DENIED_TO_UPDATE.getMessage());
        }
    }
}
