package project.pariamentApi;

import com.google.gson.Gson;
import project.auth.BasicAuthHelper;
import project.data.classes.*;
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
        // edit protocol
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
        // create protocol
        post("/rest/parliament/protocol/", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            PlenaryProtocol protocol = new Gson().fromJson(request.body(), PlenaryProtocol.class);
            if(parliamentService.protocolExists(protocol.getId())){
                halt(400, "Protocol already exists");
            }
            return parliamentService.addProtocol(protocol);
        }, gson::toJson);
        // delete protocol
        delete("/rest/parliament/protocol/:id", (request, response) -> {
            checkAuthorizationManagerLevel(request, response);
            String id = request.params(":id");
            parliamentService.deleteProtocol(id);
            return "ok";
        }, gson::toJson);








        // speaker:
        // get overview
        get("/rest/parliament/speaker/overview", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            return parliamentService.getSpeakerOverview();
        }, gson::toJson);
        // get speaker
        get("/rest/parliament/speaker/:id", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String id = request.params(":id");
            return parliamentService.getSpeaker(id);
        }, gson::toJson);
        // edit speaker
        put("/rest/parliament/speaker/:id", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String id = request.params(":id");
            if(! parliamentService.speakerExists(id)){
                halt(404, "Speaker does not exist");
            }
            Speaker speaker = new Gson().fromJson(request.body(), Speaker.class);
            if(! speaker.getId().equals(id)){
                halt(400, "Wrong speaker data");
            }
            return parliamentService.saveSpeaker(speaker);
        }, gson::toJson);
        // creat speaker
        post("/rest/parliament/speaker/", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            Speaker speaker = new Gson().fromJson(request.body(), Speaker.class);
            if(parliamentService.speakerExists(speaker.getId())){
                halt(400, "Speaker already exists");
            }
            return parliamentService.addSpeaker(speaker);
        }, gson::toJson);
        // delete speaker
        delete("/rest/parliament/speaker/:id", (request, response) -> {
            checkAuthorizationManagerLevel(request, response);
            String id = request.params(":id");
            parliamentService.deleteSpeaker(id);
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
