import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final Map<String, String> users = new HashMap<>();

    public static synchronized String authenticate(String username, String password) {

        if (users.containsKey(username)) {
            if (users.get(username).equals(password)) {
                return "OK";
            } else {
                return "ERROR_PASSWORD";
            }
        } else {
            users.put(username, password);
            return "ACCOUNT_CREATED";
        }
    }
}