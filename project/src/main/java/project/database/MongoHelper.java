package project.database;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import project.data.classes.*;

import java.util.ArrayList;
import java.util.List;

public class MongoHelper {

    public static Document toMongoDocument(PlenaryProtocol plenaryProtocol) {

        Document mongoDocument = new Document();
        mongoDocument.put("_id", plenaryProtocol.getId());
        mongoDocument.put("dokumentart", "Plenarprotokoll");
        mongoDocument.put("title", plenaryProtocol.getTitle());
        mongoDocument.put("wahlperiode", plenaryProtocol.getElectionPeriod());
        mongoDocument.put("datum", plenaryProtocol.getDate());
        mongoDocument.put("startzeit", plenaryProtocol.getStartTime());
        mongoDocument.put("endzeit", plenaryProtocol.getEndTime());
        mongoDocument.put("standort", plenaryProtocol.getPlace());

        List<Document> agenditems = new ArrayList<>();
        for (AgendaItem item : plenaryProtocol.getAgendaItems()) {
            agenditems.add(toMongoDocument(item));
        }
        mongoDocument.put("agendaItems", agenditems);


        return mongoDocument;

    }

    public static Document toMongoDocument(AgendaItem agendaItem) {
        Document document = new Document().append("index", agendaItem.getIndex()).append("title", agendaItem.getTitle());
        List<Document> speeches = new ArrayList<>();
        for (Speech speech : agendaItem.getSpeeches()) {
            speeches.add(toMongoDocument(speech));
        }
        document.put("speeches", speeches);
        return document;
    }

    public static Document toMongoDocument(Speaker speaker) {

        Document mongoDocument = new Document();
        mongoDocument.put("_id", speaker.getId());
        mongoDocument.put("name", speaker.getName());
        mongoDocument.put("firstName", speaker.getFirstName());
        mongoDocument.put("title", speaker.getTitle());
        mongoDocument.put("geburtsdatum", speaker.getBirthday());
        mongoDocument.put("geburtsort", speaker.getBirthPlace());
        mongoDocument.put("sterbedatum", speaker.getDeathDate());
        mongoDocument.put("geschlecht", speaker.getGender());
        mongoDocument.put("beruf", speaker.getJob());
        mongoDocument.put("akademischertitel", speaker.getAcademicTitle());
        mongoDocument.put("familienstand", speaker.getFamilyState());
        mongoDocument.put("religion", speaker.getReligion());
        mongoDocument.put("image", speaker.getImage());

//        List<Integer> iAbsendes = new ArrayList<>();
//        for (PlenaryProtocol absence : speaker.getAbsences()) {
//            iAbsendes.add(absence.getIndex());
//        }
//
//        mongoDocument.put("absence", iAbsendes);
        if (speaker.getParty() != null) {
            mongoDocument.put("party", speaker.getParty().getName());
        }
        if (speaker.getFraction() != null) {
            mongoDocument.put("fraction", speaker.getFraction().getName());
        }
        mongoDocument.put("role", speaker.getRole());
        return mongoDocument;

    }

    public static Document toMongoDocument(Speech speech) {

        Document mongoDocument = new Document();
        mongoDocument.put("_id", speech.getId());
        mongoDocument.put("agendaItem", speech.getAgendaItem().getIndex());
        mongoDocument.put("speaker", speech.getSpeaker().getId());
        List<Document> texts = new ArrayList<>();

        for (Text text : speech.getTexts()) {
            String type = text instanceof Comment ? "comment" : "text";
            texts.add(new Document().append("type", type).append("text", text.getText()));
        }

        mongoDocument.put("texte", texts);

        return mongoDocument;

    }


}
