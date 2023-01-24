package project.userManagement;

import com.google.gson.Gson;
import project.auth.BasicAuthHelper;

import static spark.Spark.*;

public class UserManagement {

    private final UserService userService;
    private final BasicAuthHelper basicAuthHelper;

    public UserManagement(UserService userService) {
        this.userService = userService;
        this.basicAuthHelper = new BasicAuthHelper(userService);
    }

    public void initApi() {
        Gson gson = new Gson();
        get("/rest/users/:id", (request, response) -> {
            checkAuthorization();
            String id = request.params(":id");
            return userService.getUser(id);
        }, gson::toJson);
    }

    private void checkAuthorization() {
        before((request, response) -> {

            User user = basicAuthHelper.getAuthenticatedUser(request);
            // 4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
            String group = user.getGroup();
            if (!group.equals("admin")) {
                halt(405, "Not Authorized");
            }
        });
    }
}
