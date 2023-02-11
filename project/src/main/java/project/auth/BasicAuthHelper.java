package project.auth;

import org.jetbrains.annotations.Nullable;
import project.exception.DataBaseException;
import project.userApi.User;
import project.userApi.UserService;
import spark.Request;
import spark.Response;

import java.util.Base64;

import static spark.Spark.halt;

/**
 *
 */
public class BasicAuthHelper {

    private UserService userService;

    public BasicAuthHelper(UserService userService) {
        this.userService = userService;
    }

    // extract Credentials from Header
    private String[] extractCredentials(String encodedHeader) {
        encodedHeader = getCredential(encodedHeader);
        if (encodedHeader == null) return null;
        if (!encodedHeader.equals("")) {
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return decodedHeader.split(":");
        } else {
            return null;
        }

    }

    @Nullable
    private static String getCredential(String encodedHeader) {
        String prefix = "Basic ";
        if (encodedHeader.startsWith(prefix)) {
            encodedHeader = encodedHeader.substring(prefix.length());
        } else {
            return null;
        }
        return encodedHeader;
    }

    public User getCurrentUser(Request request) throws DataBaseException {
        // 1) backend gets username and password from request Authentication header
        String encodedHeader = request.headers("Authorization");
        if (encodedHeader == null) {
            halt(401, "Not Authenticated");
            return null;
        }
        String[] credentials = extractCredentials(encodedHeader);
        if (credentials == null) {
            halt(401, "Not Authenticated");
            return null;
        }
        // 2) backend gets user from database
        String userId = credentials[0];
        User user = userService.getUser(userId);
        // if not exists
        if (user == null) {
            halt(401, "Not Authenticated");
            return null;
        }
        // 3) backend compares password
        if (!user.checkCredential(getCredential(encodedHeader))) {
            halt(401, "Not Authenticated");
            return null;
        }
        return user;
    }

    public void checkAuthorization(Request request, Response response, String forGroup) {
        User user = null;
        try {
            user = getCurrentUser(request);
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
        // 4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
        String group = user.getGroup();
        switch (forGroup) {
            case "admin":
                if (!group.equals("admin")) {
                    halt(403, "Not Authorized");
                }
                break;
            case "manager":
                if (!group.equals("manager") && !group.equals("admin")) {
                    halt(403, "Not Authorized");
                }
                break;
            case "user":
                if (!group.equals("user") & !group.equals("manager") && !group.equals("admin")) {
                    halt(403, "Not Authorized");
                }
                break;
            default:
                halt(403, "Not Authorized");
        }
    }
}
