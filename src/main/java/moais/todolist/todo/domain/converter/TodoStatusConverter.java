package moais.todolist.todo.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import moais.todolist.todo.domain.TodoStatus;

@Converter
public class TodoStatusConverter implements AttributeConverter<TodoStatus, String> {

    @Override
    public String convertToDatabaseColumn(TodoStatus todoStatus) {
        return todoStatus.getPersistValue();
    }

    @Override
    public TodoStatus convertToEntityAttribute(String persistValue) {
        return TodoStatus.findByPersistValue(persistValue);
    }
}