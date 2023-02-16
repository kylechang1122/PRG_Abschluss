package project.pariamentApi;

import com.google.gson.*;
import project.auth.BasicAuthHelper;
import project.data.classes.*;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;
import project.userApi.UserService;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static spark.Spark.*;

public class ParliamentApi {

    private class ProtocolExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipField(FieldAttributes fa) {
            String className = fa.getDeclaringClass().getName();
            String fieldName = fa.getName();
            boolean agendaItemProtocol = className.equals("project.data.classes.AgendaItem")
                    && fieldName.equals("protocol");
            boolean speechAgendaItem = className.equals("project.data.classes.Speech")
                    && fieldName.equals("agendaItem");
            boolean speechProtocol = className.equals("project.data.classes.Speech")
                    && fieldName.equals("protocol");
            boolean speechSpeaker = className.equals("project.data.classes.Speech")
                    && fieldName.equals("speaker");
            boolean protocolSpeeches = className.equals("project.data.classes.PlenaryProtocol")
                    && fieldName.equals("speeches");
            boolean speakerSpeeches = className.equals("project.data.classes.Speaker")
                    && fieldName.equals("speeches");
            boolean partyMembers = className.equals("project.data.classes.Party")
                    && fieldName.equals("members");
            return (agendaItemProtocol || speechAgendaItem || speechProtocol || speechSpeaker || protocolSpeeches || speakerSpeeches || partyMembers);
        }

        @Override
        public boolean shouldSkipClass(Class<?> type) {
            // never skips any class
            return false;
        }
    }
    private static final String UI_DATE_FORMAT = "yyyy-MM-dd";
    private static final String UI_TIME_FORMAT = "HH:mm";

    /**
     * from https://stackoverflow.com/questions/29630028/gson-time-deserilization
     */
    private class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public java.sql.Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            try {
                Date date = new SimpleDateFormat(UI_DATE_FORMAT, Locale.GERMAN).parse(jsonElement.getAsString());
                return new java.sql.Date(date.getTime());
            } catch (ParseException e) {
            }

            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + UI_DATE_FORMAT);
        }
    }
    /**
     * from https://stackoverflow.com/questions/29630028/gson-time-deserilization
     */
    private class TimeDeserializer implements JsonDeserializer<Time> {

        @Override
        public Time deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            try {

                Date date = new SimpleDateFormat(UI_TIME_FORMAT, Locale.GERMAN).parse(jsonElement.getAsString());
                Time t = new Time(date.getTime());
                return t;
            } catch (ParseException e) {
            }
            throw new JsonParseException("Unparseable time: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + UI_TIME_FORMAT);
        }
    }

    private class TimeSerializer implements JsonSerializer<Time> {
        @Override
        public JsonElement serialize(Time time, Type type, JsonSerializationContext jsonSerializationContext) {
            String formattedTime = new SimpleDateFormat(UI_TIME_FORMAT, Locale.GERMAN).format(time.getTime());
            return new JsonPrimitive(formattedTime);
        }
    }

    private class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            String formattedTime = new SimpleDateFormat(UI_DATE_FORMAT, Locale.GERMAN).format(date.getTime());
            return new JsonPrimitive(formattedTime);
        }
    }

    private class TextSerializer implements JsonSerializer<Text> {

        @Override
        public JsonElement serialize(Text text, Type type, JsonSerializationContext jsonSerializationContext) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", text.getText());
            if(text.getClass().getName().equals("project.data.classes.Comment")){
                jsonObject.addProperty("type", "comment");
            } else {
                jsonObject.addProperty("type", "text");
            }
            return jsonObject;
        }
    }

    private class TextDeserializer implements JsonDeserializer<Text> {

        @Override
        public Text deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext context) throws JsonParseException {
            try {
                JsonObject json = jsonElement.getAsJsonObject();
                String typeProp = String.valueOf(json.get("type"));
                String content = String.valueOf(json.get("text"));
                if (typeProp.equals("comment")){
                   return new Comment(content);
                } else {
                    return new Text(content);
                }
            } catch (Exception e) {
                throw new JsonParseException("Unparseable text: \"" + jsonElement.getAsString());
            }
        }
    }

    private final ParliamentService parliamentService;
    private final BasicAuthHelper basicAuthHelper;

    private final Gson gson;

    public ParliamentApi(UserService userService, MongoDBHandler dbConnection) {
        this.parliamentService = new ParliamentService(new ParliamentDbHandler(dbConnection));
        this.basicAuthHelper = new BasicAuthHelper(userService);
        GsonBuilder gSonBuilder=  new GsonBuilder();
        gSonBuilder.registerTypeAdapter(java.sql.Date.class, new DateDeserializer());
        gSonBuilder.registerTypeAdapter(java.sql.Date.class, new DateSerializer());
        gSonBuilder.registerTypeAdapter(Time.class, new TimeDeserializer());
        gSonBuilder.registerTypeAdapter(Time.class, new TimeSerializer());
        gSonBuilder.registerTypeHierarchyAdapter(Text.class, new TextSerializer());
        gSonBuilder.registerTypeHierarchyAdapter(Text.class, new TextDeserializer());
        gSonBuilder.setExclusionStrategies(new ProtocolExclusionStrategy());
        gson = gSonBuilder.create();
    }

    /**
     * start rest api for parliament data
     */
    public void initApi() {
        //protocol
        initProtocolApi();
        // agenda
        initAgendaItemApi();
        //speeches
        initSpeechApi();
        // speaker:
        initSpeakerApi();

    }

    private void initProtocolApi() {
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
            PlenaryProtocol protocol = gson.fromJson(request.body(), PlenaryProtocol.class);
            if(! protocol.getId().equals(id)){
                halt(400, "Wrong protocol data");
            }
            return parliamentService.updateProtocolMetaData(protocol);
        }, gson::toJson);
        // create protocol
        post("/rest/parliament/protocol", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            PlenaryProtocol protocol = gson.fromJson(request.body(), PlenaryProtocol.class);
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
    }

    private void initSpeechApi() {
        // get speech
        get("/rest/parliament/protocol/:id/speech/:sid", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String speechId = request.params(":sid");
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            try{
                return parliamentService.getSpeech(protocolId, speechId);
            } catch (IllegalArgumentException e) {
                halt(404, "Speech does not exist");
            }
            return null;
        }, gson::toJson);
        // update speech
        put("/rest/parliament/protocol/:id/speech/:sid", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String speechId = request.params(":sid");
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            Speech speech = gson.fromJson(request.body(), Speech.class);
            if(! speech.getId().equals(speechId)){
                halt(400, "Wrong speech data");
            }
            try{
                return parliamentService.updateSpeech(protocolId, speech);
            } catch (IllegalArgumentException e) {
                halt(404, "Speech does not exist");
            }
            return null;
        }, gson::toJson);
        // create speech
        post("/rest/parliament/protocol/:id/agenda-item/:aid/speeches/:num", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String agendaItemIndexString = request.params(":aid");
            int number = Integer.parseInt(request.params(":num"));
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            Speech speech = gson.fromJson(request.body(), Speech.class);
            try{
                return parliamentService.createSpeech(protocolId, agendaItemIndexString, number, speech);
            } catch (IllegalArgumentException e) {
                halt(404, "Speech does not exist");
            }
            return null;
        }, gson::toJson);
        // delete speech
        delete("/rest/parliament/protocol/:id/speech/:sid", (request, response) -> {
            checkAuthorizationManagerLevel(request, response);
            String id = request.params(":id");
            String speechId = request.params(":sid");
            parliamentService.deleteSpeech(id, speechId);
            return "ok";
        }, gson::toJson);
    }

    private void initAgendaItemApi() {
        // get overview
        get("/rest/parliament/protocol/:id/agenda/overview", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String id = request.params(":id");
            return parliamentService.getAgendaItemsOverview(id);
        }, gson::toJson);
        // get agendaItem
        get("/rest/parliament/protocol/:id/agenda-item/:aid", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String agendaItemId = request.params(":aid");
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            try{
                AgendaItem agendaItem = parliamentService.getAgendaItem(protocolId, agendaItemId);
                if(agendaItem == null) {
                    halt(404, "Agenda item does not exist");
                }
                return agendaItem;

            } catch (IllegalArgumentException e) {
                halt(404, "Speech does not exist");
            }
            return null;
        }, gson::toJson);
        // update agendaItem
        put("/rest/parliament/protocol/:id/agenda-item/:aid/:num", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String agendaItemId = request.params(":aid");
            int index = Integer.parseInt(request.params(":num"));
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            AgendaItem agendaItem = gson.fromJson(request.body(), AgendaItem.class);
            if(! agendaItem.getId().equals(agendaItemId)){
                halt(400, "Wrong agenda-item data");
            }
            try{
                return parliamentService.updateAgendaItem(protocolId, index, agendaItem);
            } catch (IllegalArgumentException e) {
                halt(404, "AgendaItem does not exist");
            }
            return null;
        }, gson::toJson);
        // create agendaItem
        post("/rest/parliament/protocol/:id/agenda/:num", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            int index = Integer.parseInt(request.params(":num"));
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            AgendaItem agendaItem = gson.fromJson(request.body(), AgendaItem.class);
            return parliamentService.createAgendaItem(protocolId, index, agendaItem);
        }, gson::toJson);
        // delete agendaItem
        delete("/rest/parliament/protocol/:id/agenda-item/:aid", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            String protocolId = request.params(":id");
            String agendaItemId = request.params(":aid");
            if(! parliamentService.protocolExists(protocolId)){
                halt(404, "Protocol does not exist");
            }
            parliamentService.deleteAgendaItem(protocolId, agendaItemId);
            return "success";
        }, gson::toJson);
    }

    private void initSpeakerApi() {
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
            return parliamentService.updateSpeaker(speaker);
        }, gson::toJson);
        // creat speaker
        post("/rest/parliament/speaker/", (request, response) -> {
            checkAuthorizationUserLevel(request, response);
            Speaker speaker = new Gson().fromJson(request.body(), Speaker.class);
            if(parliamentService.speakerExists(speaker.getId())){
                halt(400, "Speaker already exists");
            }
            return parliamentService.createSpeaker(speaker);
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
