package project.pariamentApi;

import com.google.gson.Gson;
import org.bson.Document;
import project.auth.BasicAuthHelper;
import project.exception.DataBaseException;
import project.userApi.User;
import project.userApi.UserService;
import spark.Request;
import spark.Response;

import java.util.Base64;

import static spark.Spark.*;

public class ParliamentApi {

    private final UserService userService;
    private final BasicAuthHelper basicAuthHelper;

    public ParliamentApi(UserService userService) {
        this.userService = userService;
        this.basicAuthHelper = new BasicAuthHelper(userService);
    }

    /**
     * start rest api for parliament data
     */
    public void initApi() {
        Gson gson = new Gson();
        // get protocol
        get("/rest/parliament/protocol/:id", (request, response) -> {
            checkAuthorization(request, response);
            String id = request.params(":id");
            return userService.getUser(id);
        }, gson::toJson);
        put("/rest/admin/users/:id", (request, response) -> {
            checkAuthorization(request, response);
            String id = request.params(":id");
            if(! userService.userExists(id)){
                halt(404, "User does not exist");
            }
            User user = new Gson().fromJson(request.body(), User.class);
            if(! user.getUserId().equals(id)){
                halt(400, "Wrong user data");
            }
            return userService.editUser(user);
        }, gson::toJson);
        post("/rest/admin/users/", (request, response) -> {
            checkAuthorization(request, response);
            User user = new Gson().fromJson(request.body(), User.class);
            if(userService.userExists(user.getUserId())){
                halt(400, "User already exists");
            }
            return userService.addUser(user);
        }, gson::toJson);
    }

    private void checkAuthorization(Request request, Response response) throws DataBaseException {
            User user = basicAuthHelper.getCurrentUser(request);
            // 4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
            String group = user.getGroup();
            if (!group.equals("manager")) {
                halt(403, "Not Authorized");
            }
    }
}
