package project.pariamentApi;

import org.bson.Document;
import project.data.classes.*;
import project.database.MongoHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class ParliamentService {

    private ParliamentDbHandler dbConnection;

    public ParliamentService(ParliamentDbHandler mongoDBHandler) {

        this.dbConnection = mongoDBHandler;
    }

    public PlenaryProtocol getProtocol(String id) {
        Document document = dbConnection.getProtocol(id);
        return new PlenaryProtocol(document);
    }

    public PlenaryProtocol addProtocol(PlenaryProtocol plenaryProtocol) {
        Document document = dbConnection.createProtocol(MongoHelper.toMongoDocument(plenaryProtocol));
        plenaryProtocol.setId(document.getString("_id"));
        return plenaryProtocol;
    }

    public PlenaryProtocol saveProtocol(PlenaryProtocol plenaryProtocol) {
        Document document = dbConnection.updateProtocol(MongoHelper.toMongoDocument(plenaryProtocol));
        return plenaryProtocol;
    }

    public void deleteProtocol(String id) {
        dbConnection.deletetProtocol(id);
    }

    public ArrayList<Document> getProtocolOverview() {
        return dbConnection.getProtocolCollection().aggregate(Arrays.asList(new Document("$project",
                new Document("title", 1L)
                        .append("_id", 1L)))).into(new ArrayList<>());
    }

    public boolean protocolExists(String id) {
        return dbConnection.protocolExists(id);
    }

    //speaker:
    public Speaker getSpeaker(String id) {
        Document document = dbConnection.getSpeaker(id);
        return new Speaker(document);
    }

    public Speaker addSpeaker(Speaker speaker) {
        Document document = dbConnection.createSpeaker(MongoHelper.toMongoDocument(speaker));
        speaker.setId(document.getString("_id"));
        return speaker;
    }

    public Speaker saveSpeaker(Speaker speaker) {
        Document document = dbConnection.updateSpeaker(MongoHelper.toMongoDocument(speaker));
        return new Speaker(document);
    }

    public void deleteSpeaker(String id) {
        dbConnection.deletetSpeaker(id);
    }

    public ArrayList<Document> getSpeakerOverview() {
        return dbConnection.getSpeakerCollection().aggregate(Arrays.asList(new Document("$project",
                new Document("_id", 1L)
                        .append("firstName", 1L)
                        .append("name", 1L)
                        .append("title", 1L)
                        .append("role", 1L)
        ))).into(new ArrayList<>());
    }

    public boolean speakerExists(String id) {
        return dbConnection.speakerExists(id);
    }
}
