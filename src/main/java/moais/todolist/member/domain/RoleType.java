package moais.todolist.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moais.todolist.global.exception.ErrorMessage;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum RoleType implements GrantedAuthority {

    ROLE_ADMIN("ROLE_ADMIN", "admin"),

    ROLE_USER("ROLE_USER", "user");

    private final String authority;

    private final String code;

    public static RoleType findByAuthority(String roleType) {
        for (RoleType value : values()) {
            if (value.authority.equals(roleType)) {
                return value;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_ROLE_TYPE.getMessage());
    }
}
