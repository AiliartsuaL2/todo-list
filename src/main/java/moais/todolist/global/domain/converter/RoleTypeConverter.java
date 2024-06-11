package moais.todolist.global.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import moais.todolist.member.domain.RoleType;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType roleType) {
        return roleType.getAuthority();
    }

    @Override
    public RoleType convertToEntityAttribute(String roleType) {
        return RoleType.findByAuthority(roleType);
    }
}
