package moais.todolist.member.domain;

import static moais.todolist.member.utils.PasswordUtils.B_CRYPT_PASSWORD_ENCODER;
import static moais.todolist.member.utils.PasswordUtils.PASSWORD_PATTERN;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moais.todolist.global.domain.BaseEntity;
import moais.todolist.global.domain.converter.BooleanToYNConverter;
import moais.todolist.member.exception.ErrorMessage;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String loginId;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean deleteYn;

    private LocalDateTime deletedAt;

    public Member(String nickname, String loginId, String password) {
        validateNicknameInput(nickname);
        validateLoginIdInput(loginId);
        validatePasswordInput(password);

        this.nickname = nickname;
        this.loginId = loginId;
        this.password = B_CRYPT_PASSWORD_ENCODER.encode(password);
        this.deleteYn = Boolean.FALSE;
    }

    private void validatePasswordInput(String password) {
        notNullValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PASSWORD_REGEX.getMessage());
        }
    }

    private void validateLoginIdInput(String loginId) {
        notNullValidation(loginId, ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        if (loginId.length() > 30) {
            throw new IllegalArgumentException(ErrorMessage.OVER_THAN_MAX_SIZE_AT_LOGIN_ID.getMessage());
        }
    }

    private void validateNicknameInput(String nickname) {
        notNullValidation(nickname, ErrorMessage.NOT_EXIST_NICKNAME.getMessage());
        if (nickname.length() > 30) {
            throw new IllegalArgumentException(ErrorMessage.OVER_THAN_MAX_SIZE_AT_NICKNAME.getMessage());
        }
    }

    public String signIn(String password) {
        validatePassword(password);
        // 단위 테스트를 위해 getId 사용
        return this.getId();
    }

    public void withdraw(String memberId, String loginId, String password) {
        validateLoginId(loginId);
        validatePassword(password);
        validateId(memberId);

        this.deleteYn = Boolean.TRUE;
        this.deletedAt = LocalDateTime.now();
    }

    private void validateLoginId(String loginId) {
        notNullValidation(loginId, ErrorMessage.NOT_EXIST_LOGIN_ID.getMessage());
        if (!this.loginId.equals(loginId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MATCHED_LOGIN_ID.getMessage());
        }
    }

    public void validateId(String id) {
        notNullValidation(id, ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        // 단위 테스트를 위해 getId 사용
        if (!this.getId().equals(id)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MATCHED_ID.getMessage());
        }
    }

    private void validatePassword(String password) {
        notNullValidation(password, ErrorMessage.NOT_EXIST_PASSWORD.getMessage());
        if (!isMatchedPassword(password)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }
    }

    boolean isMatchedPassword(String password) {
        return B_CRYPT_PASSWORD_ENCODER.matches(password, this.password);
    }
}
