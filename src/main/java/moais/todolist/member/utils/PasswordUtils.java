package moais.todolist.member.utils;

import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    public static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static final String PASSWORD_REGEX = "^(?=.*[!@#$%])(?=.*\\d)(?=.*[a-z])[A-Za-z\\d!@#$%]{8,}$";

    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
}
