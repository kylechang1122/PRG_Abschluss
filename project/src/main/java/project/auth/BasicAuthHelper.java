package project.auth;

import project.exception.DataBaseException;
import project.userManagement.User;
import project.userManagement.UserService;
import spark.Request;

import java.util.Base64;

import static spark.Spark.halt;

/**
 *
 */
public class BasicAuthHelper {

    private UserService userService;
    public BasicAuthHelper(UserService userService) {
    }

    // extract Credentials from Header
    private String[] extractCredentials(String encodedHeader) {
        if (encodedHeader != null) {
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return decodedHeader.split(":");
        } else {
            return null;
        }

    }

    public User getAuthenticatedUser(Request request) throws DataBaseException {
        // 1) backend gets username and password from request Authentication header
        String encodedHeader = request.headers("Authorization");
        if(encodedHeader == null) {
            halt(401, "Not Authenticated");
        }
        encodedHeader = encodedHeader.substring(encodedHeader.lastIndexOf("Basic") + 1);
        String[] credentials = extractCredentials(encodedHeader);
        // 2) backend gets user from database
        User user = userService.getUser(credentials[0]);
        // if not exists
        if (user == null) {
            halt(401, "Not Authenticated");
        }
        // 3) backend compares password
        if (!user.checkPassword(credentials[1])) {
            halt(401, "Not Authenticated");
        }
        return user;
    }
}
