package project.userApi;

import com.google.gson.Gson;
import project.auth.BasicAuthHelper;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;
import spark.Request;
import spark.Response;

import org.bson.Document;

import java.util.Base64;

import static spark.Spark.*;

public class UserApi {

    private final UserService userService;
    private final BasicAuthHelper basicAuthHelper;

    public UserApi(UserService userService) {
        this.userService = userService;
        this.basicAuthHelper = new BasicAuthHelper(userService);
    }

    public void initApi() {
        Gson gson = new Gson();
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
        basicAuthHelper.checkAuthorization(request, response, "admin");
    }
}
