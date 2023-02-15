package project.pariamentApi;

import org.bson.Document;
import project.data.classes.*;
import project.database.MongoHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParliamentService {

    static private final ArrayList<Document> agendaOverview = new ArrayList<>(
            Arrays.asList(new Document("$unwind",
                            new Document("path", "$agendaItems")
                                    .append("includeArrayIndex", "number")
                                    .append("preserveNullAndEmptyArrays", true)),
                    new Document("$project",
                            new Document("number", 1L)
                                    .append("index", "$agendaItems.index")
                                    .append("title", "$agendaItems.title")))
    );

    private final ParliamentDbHandler dbConnection;

    private static Speech getSpeech(String speechId, PlenaryProtocol protocol) {
        List<Speech> speeches = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speechId)).collect(Collectors.toList());
        if (speeches.isEmpty()) {
            throw new IllegalArgumentException("speech not existing");
        }
        return speeches.get(0);
    }

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

    public ArrayList<Document> getAgendaItemsOverview(String protocolId) {
        // match protocol
        List<Document> aggregate = new ArrayList<>(
                Collections.singletonList(
                        new Document("$match",
                                new Document("_id",
                                        new Document("$eq", protocolId))))
        );
        // query agenda overview
        aggregate.addAll(agendaOverview);
        // sort
        aggregate.add(new Document("$sort", new Document("number",1L)));
        return dbConnection.getProtocolCollection().aggregate(aggregate).into(new ArrayList<>());
    }

    public AgendaItem getAgendaItem(String protocolId, String agendaIndex) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        return protocol.getAgendaItemByIndexString(agendaIndex);
    }

    public AgendaItem createAgendaItem(String protocolId, int index, AgendaItem agendaItem) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        agendaItem.setProtocol(protocol);
        protocol.getAgendaItems().set(index, agendaItem);
        updateProtocol(protocol);
        return agendaItem;
    }

    public AgendaItem updateAgendaItem(String protocolId, int index, AgendaItem agendaItem) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        AgendaItem existingAgendaItem = protocol.getAgendaItems().get(index);
        agendaItem.getSpeeches().addAll(existingAgendaItem.getSpeeches());
        agendaItem.setProtocol(protocol);
        protocol.getAgendaItems().set(index, agendaItem);
        updateProtocol(protocol);
        return agendaItem;
    }

    public void deleteAgendaItem(String protocolId, String agendaItemIndexString) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        AgendaItem existingAgendaItem = protocol.getAgendaItemByIndexString(agendaItemIndexString);
        if (existingAgendaItem == null) {
            throw new IllegalArgumentException("agendaItem not existing");
        }
        protocol.getAgendaItems().remove(existingAgendaItem);
        updateProtocol(protocol);
    }

    public Speech getSpeech(String protocolId, String speechId) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        return getSpeech(speechId, protocol);
    }

    public Speech addSpeech(String protocolId, String agendaItemIndexString, int speechNumber, Speech speech) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speech.getId())).collect(Collectors.toList());
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("speech already exists");
        }
        AgendaItem agendaItem = protocol.getAgendaItemByIndexString(agendaItemIndexString);
        speech.setProtocol(protocol);
        speech.setAgendaItem(agendaItem);
        agendaItem.getSpeeches().set(speechNumber, speech);
        updateProtocol(protocol);
        return speech;
    }

    public Speech updateSpeech(String protocolId, Speech speech) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speech.getId())).collect(Collectors.toList());
        if (existing.isEmpty()) {
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

    public void deleteSpeech(String protocolId, String speechId) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        List<Speech> existing = protocol.getSpeeches().stream().filter((s) -> s.getId().equals(speechId)).collect(Collectors.toList());
        if (existing.isEmpty()) {
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
