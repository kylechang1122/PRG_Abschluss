package project.pariamentApi;

import org.bson.Document;
import project.data.classes.*;
import project.database.MongoHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public PlenaryProtocol updateProtocol(PlenaryProtocol plenaryProtocol) {
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

    // speeches
    public ArrayList<Document> getSpeechesOverview(String protocolId) {
        return dbConnection.getProtocolCollection().aggregate(
                Arrays.asList(new Document("$match",
                                new Document("_id",
                                        new Document("$eq", protocolId))),
                        new Document("$unwind",
                                new Document("path", "$agendaItems")
                                        .append("includeArrayIndex", "index")
                                        .append("preserveNullAndEmptyArrays", false)),
                        new Document("$replaceRoot",
                                new Document("newRoot", "$agendaItems")),
                        new Document("$unwind",
                                new Document("path", "$speeches")
                                        .append("includeArrayIndex", "num")
                                        .append("preserveNullAndEmptyArrays", false)),
                        new Document("$replaceRoot",
                                new Document("newRoot", "$speeches")),
                        new Document("$lookup",
                                new Document("from", "speaker")
                                        .append("localField", "speaker")
                                        .append("foreignField", "_id")
                                        .append("as", "speaker")),
                        new Document("$project",
                                new Document("index", 1L)
                                        .append("agendaItem", 1L)
                                        .append("speaker",
                                                new Document("$first", "$speaker"))),
                        new Document("$project",
                                new Document("index", 1L)
                                        .append("agendaItem", 1L)
                                        .append("speaker._id", 1L)
                                        .append("speaker.name", 1L)
                                        .append("speaker.firstName", 1L)
                                        .append("speaker.akademischertitel", 1L)))
        ).into(new ArrayList<>());
    }

    public Speech getSpeech(String protocolId, String speechId) {
        ArrayList<Document> result = dbConnection.getProtocolCollection().aggregate(
                Arrays.asList(new Document("$match",
                                new Document("_id",
                                        new Document("$eq", protocolId))),
                        new Document("$unwind",
                                new Document("path", "$agendaItems")
                                        .append("includeArrayIndex", "index")
                                        .append("preserveNullAndEmptyArrays", false)),
                        new Document("$replaceRoot",
                                new Document("newRoot", "$agendaItems")),
                        new Document("$unwind",
                                new Document("path", "$speeches")
                                        .append("includeArrayIndex", "num")
                                        .append("preserveNullAndEmptyArrays", false)),
                        new Document("$replaceRoot",
                                new Document("newRoot", "$speeches")),
                        new Document("$match",
                                new Document("_id",
                                        new Document("$eq", speechId))),
                        new Document("$lookup",
                                new Document("from", "speaker")
                                        .append("localField", "speaker")
                                        .append("foreignField", "_id")
                                        .append("as", "speaker")),
                        new Document("$project",
                                new Document("index", 1L)
                                        .append("texte", 1L)
                                        .append("agendaItem", 1L)
                                        .append("speaker",
                                                new Document("$first", "$speaker")))
                )
        ).into(new ArrayList<>());
        if (!result.isEmpty()) {
            return new Speech(result.get(0));
        }
        return null;
    }

    public Speech addSpeech(String protocolId, String agendaItemIndex, Speech speech) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speech.getId())).collect(Collectors.toList());
        if (!existing.isEmpty()){
            throw new IllegalArgumentException("speech already exists");
        }
        List<AgendaItem> agenda = protocol.getAgendaItems().stream().filter((s) -> s.getIndex().equals(agendaItemIndex)).collect(Collectors.toList());
        if (agenda.isEmpty()){
            throw new IllegalArgumentException("agenda item not existing");
        }
        AgendaItem agendaItem = agenda.get(0);
        speech.setProtocol(protocol);
        speech.setAgendaItem(agendaItem);
        agendaItem.addSpeech(speech);
        updateProtocol(protocol);
        return speech;
    }

    public Speech updateSpeech(String protocolId, Speech speech) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speech.getId())).collect(Collectors.toList());
        if (existing.isEmpty()){
            throw new IllegalArgumentException("speech does not exist");
        }
        Speech existingSpeech = existing.get(0);
        AgendaItem agendaItem = existingSpeech.getAgendaItem();
        agendaItem.removeSpeech(existingSpeech);
        speech.setProtocol(protocol);
        speech.setAgendaItem(agendaItem);
        agendaItem.addSpeech(speech);
        updateProtocol(protocol);
        return speech;
    }

    public void deleteSpeech(String protocolId,  String speechId) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speechId)).collect(Collectors.toList());
        if (existing.isEmpty()){
            throw new IllegalArgumentException("speech does not exist");
        }
        Speech existingSpeech = existing.get(0);
        AgendaItem agendaItem = existingSpeech.getAgendaItem();
        agendaItem.removeSpeech(existingSpeech);
        updateProtocol(protocol);
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
