package project.userManagement;

import com.google.gson.Gson;
import project.auth.BasicAuthHelper;
import project.exception.DataBaseException;
import spark.Request;
import spark.Response;

import org.bson.Document;

import java.util.Base64;

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
        initCreateUser();
        get("/authenticate", (request, response) -> {
            Document user = basicAuthHelper.getCurrentUser(request).toDocument();
            return user;
        }, gson::toJson);
        get("/rest/admin/users/:id", (request, response) -> {
            checkAuthorization(request, response);
            String id = request.params(":id");
            return userService.getUser(id);
        }, gson::toJson);
        put("/rest/admin/users/:id", (request, response) -> {
            checkAuthorization(request, response);
            String id = request.params(":id");
            // todo edit user
            return userService.getUser(id);
        }, gson::toJson);
        post("/rest/admin/users/:id", (request, response) -> {
            checkAuthorization(request, response);
            String id = request.params(":id");
            // todo edit user
            return userService.getUser(id);
        }, gson::toJson);
    }

    private void checkAuthorization(Request request, Response response) throws DataBaseException {
            User user = basicAuthHelper.getCurrentUser(request);
            // 4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
            String group = user.getGroup();
            if (!group.equals("admin")) {
                halt(403, "Not Authorized");
            }
    }

    private void initCreateUser(){
        Document admin = new Document();
        admin.append("_id", "admin");
        admin.append("group", "admin");
        admin.append("credential", Base64.getEncoder().encode("admin:bla1123".getBytes()));
        userService.addUser(new User(admin));
    }
}
