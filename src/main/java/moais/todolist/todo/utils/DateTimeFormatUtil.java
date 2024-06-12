package moais.todolist.todo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String toString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DateTimeFormatUtil.DATE_TIME_FORMAT));
    }
}
