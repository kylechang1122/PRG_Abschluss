package project.pariamentApi;

import com.google.gson.Gson;
import project.auth.BasicAuthHelper;
import project.data.classes.PlenaryProtocol;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;
import project.userApi.UserService;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class ParliamentApi {

    private final ParliamentService parliamentService;
    private final BasicAuthHelper basicAuthHelper;

    public ParliamentApi(UserService userService, MongoDBHandler dbConnection) {
        this.parliamentService = new ParliamentService(new ParliamentDbHandler(dbConnection));
        this.basicAuthHelper = new BasicAuthHelper(userService);
    }

    /**
     * start rest api for parliament data
     */
    public void initApi() {
        Gson gson = new Gson();
        // get overview
        get("/rest/parliament/protocol/overview", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            return parliamentService.getProtocolOverview();
        }, gson::toJson);
        // get protocol
        get("/rest/parliament/protocol/:id", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String id = request.params(":id");
            return parliamentService.getProtocol(id);
        }, gson::toJson);
        put("/rest/parliament/protocol/:id", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String id = request.params(":id");
            if(! parliamentService.protocolExists(id)){
                halt(404, "Protocol does not exist");
            }
            PlenaryProtocol protocol = new Gson().fromJson(request.body(), PlenaryProtocol.class);
            if(! protocol.getId().equals(id)){
                halt(400, "Wrong protocol data");
            }
            return parliamentService.saveProtocol(protocol);
        }, gson::toJson);
        post("/rest/parliament/protocol/", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            PlenaryProtocol protocol = new Gson().fromJson(request.body(), PlenaryProtocol.class);
            if(parliamentService.protocolExists(protocol.getId())){
                halt(400, "User already exists");
            }
            return parliamentService.addProtocol(protocol);
        }, gson::toJson);
        delete("/rest/parliament/protocol/:id", (request, response) -> {
            checkAuthorizationManagerLevel(request, response);
            String id = request.params(":id");
            parliamentService.deleteProtocol(id);
            return "ok";
        }, gson::toJson);
    }

    private void checkAuthorizationUserLevel(Request request, Response response) throws DataBaseException {
        basicAuthHelper.checkAuthorization(request, response, "user");
    }

    private void checkAuthorizationManagerLevel(Request request, Response response) throws DataBaseException {
        basicAuthHelper.checkAuthorization(request, response, "manager");
    }
}
