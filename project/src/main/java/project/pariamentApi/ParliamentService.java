package project.pariamentApi;

import org.bson.Document;
import org.bson.types.ObjectId;
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
                                    .append("title", "$agendaItems.title")
                                    .append("_id", "$agendaItems._id")
                    ))
    );

    private final ParliamentDbHandler dbConnection;

    private static Speech getSpeechById(String speechId, PlenaryProtocol protocol) {
        List<Speech> speeches = protocol.getSpeeches().stream().filter((s) -> {
            String ID = s.getId();
            if (ID == null) {
                return false;
            }
            return ID.equals(speechId);
        }).collect(Collectors.toList());
        if (speeches.isEmpty()) {
            return null;
        }
        return speeches.get(0);
    }

    private static AgendaItem getAgendaItemById(String id, PlenaryProtocol protocol) {
        List<AgendaItem> agendaItems = protocol.getAgendaItems().stream().filter((a) -> {
            String ID = a.getId();
            if (ID == null) {
                return false;
            }
            return ID.equals(id);
        }).collect(Collectors.toList());
        if (agendaItems.isEmpty()) {
            return null;
        }
        return agendaItems.get(0);
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

    public PlenaryProtocol updateProtocolMetaData(PlenaryProtocol plenaryProtocol) {
        PlenaryProtocol existingProtocol = getProtocol(plenaryProtocol.getId());
        plenaryProtocol.setAgendaItems(existingProtocol.getAgendaItems());
        updateProtocol(plenaryProtocol);
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
        aggregate.add(new Document("$sort", new Document("number", 1L)));
        return dbConnection.getProtocolCollection().aggregate(aggregate).into(new ArrayList<>());
    }

    public AgendaItem getAgendaItem(String protocolId, String agendaIndex) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        return getAgendaItemById(agendaIndex, protocol);
    }

    public AgendaItem createAgendaItem(String protocolId, int position, AgendaItem agendaItem) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        agendaItem.setId(new ObjectId().toString());
        agendaItem.setProtocol(protocol);
        if (position > protocol.getAgendaItems().size()) {
            position = protocol.getAgendaItems().size();
        }
        protocol.getAgendaItems().add(position, agendaItem);
        updateProtocol(protocol);
        return agendaItem;
    }

    public AgendaItem updateAgendaItem(String protocolId, int position, AgendaItem agendaItem) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        AgendaItem existingAgendaItem = getAgendaItemById(agendaItem.getId(), protocol);
        agendaItem.getSpeeches().addAll(existingAgendaItem.getSpeeches());
        agendaItem.setProtocol(protocol);
        protocol.getAgendaItems().remove(existingAgendaItem);
        if (position > protocol.getAgendaItems().size()) {
            position = protocol.getAgendaItems().size();
        }
        protocol.getAgendaItems().add(position, agendaItem);
        updateProtocol(protocol);
        return agendaItem;
    }

    public void deleteAgendaItem(String protocolId, String agendaItemId) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        if (protocol == null) {
            throw new IllegalArgumentException("protocol not existing");
        }
        AgendaItem existingAgendaItem = getAgendaItemById(agendaItemId, protocol);
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
        return getSpeechById(speechId, protocol);
    }

    public Speech createSpeech(String protocolId, String agendaItemId, int position, Speech speech) {
        speech.setId(new ObjectId().toString());
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        AgendaItem agendaItem = getAgendaItemById(agendaItemId, protocol);
        if (agendaItem == null) {
            throw new IllegalArgumentException("agendaItem not existing");
        }
        speech.setProtocol(protocol);
        speech.setAgendaItem(agendaItem);
        if (position > agendaItem.getSpeeches().size()) {
            position = agendaItem.getSpeeches().size();
        }
        agendaItem.getSpeeches().add(position, speech);
        updateProtocol(protocol);
        return speech;
    }

    public Speech updateSpeech(String protocolId, Speech speech) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        Speech existingSpeech = getSpeechById(speech.getId(), protocol);
        if (existingSpeech == null) {
            throw new IllegalArgumentException("speech does not exist");
        }

        AgendaItem agendaItem = existingSpeech.getAgendaItem();
        int index = agendaItem.getSpeeches().indexOf(existingSpeech);
        agendaItem.removeSpeech(existingSpeech);
        speech.setProtocol(protocol);
        speech.setAgendaItem(agendaItem);
        agendaItem.getSpeeches().add(index, speech);
        updateProtocol(protocol);
        return speech;
    }

    public void deleteSpeech(String protocolId, String speechId) {
        PlenaryProtocol protocol = this.getProtocol(protocolId);
        Speech existingSpeech = getSpeechById(speechId, protocol);
        if (existingSpeech == null) {
            throw new IllegalArgumentException("speech does not exist");
        }
        AgendaItem agendaItem = existingSpeech.getAgendaItem();
        agendaItem.removeSpeech(existingSpeech);
        updateProtocol(protocol);
    }


    //speaker:
    public Speaker getSpeaker(String id) {
        Document document = dbConnection.getSpeaker(id);
        return new Speaker(document);
    }

    public Speaker createSpeaker(Speaker speaker) {
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
