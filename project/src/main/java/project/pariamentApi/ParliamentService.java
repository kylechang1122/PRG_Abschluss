package project.pariamentApi;

import org.bson.Document;
import project.data.classes.PlenaryProtocol;
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
        return dbConnection.getCollection().aggregate(Arrays.asList(new Document("$project",
                new Document("title", 1L)
                        .append("_id", 1L)))).into(new ArrayList<>());
    }

    public boolean protocolExists(String id) {
        return dbConnection.protocolExists(id);
    }
}
