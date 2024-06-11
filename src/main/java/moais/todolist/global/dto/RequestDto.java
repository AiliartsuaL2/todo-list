package moais.todolist.global.dto;

import org.springframework.util.ObjectUtils;

public interface RequestDto {
    default <T> void requiredArgumentValidation(T data, String message) {
        if(ObjectUtils.isEmpty(data)) {
            throw new IllegalArgumentException(message);
        }
    }
}
