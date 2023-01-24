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
        this.userService = userService;
    }

    // extract Credentials from Header
    private String[] extractCredentials(String encodedHeader) {
        String prefix = "Basic ";
        if (encodedHeader.startsWith(prefix)) {
            encodedHeader = encodedHeader.substring(prefix.length());
        } else {
            return null;
        }
        if (!encodedHeader.equals("")) {
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
        if (!user.checkPassword(credentials[1])) {
            halt(401, "Not Authenticated");
            return null;
        }
        return user;
    }
}
