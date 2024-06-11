package moais.todolist.global.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moais.todolist.global.domain.BaseEntity;
import moais.todolist.global.exception.ErrorMessage;
import moais.todolist.member.domain.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_ACCOUNT")
@Getter
public class UserAccount extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String memberId;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public UserAccount(String memberId, String roleType) {
        if (ObjectUtils.isEmpty(memberId)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_MEMBER_ID.getMessage());
        }
        this.roleType = RoleType.findByAuthority(roleType);
        this.memberId = memberId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.roleType);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.memberId);
    }

}
