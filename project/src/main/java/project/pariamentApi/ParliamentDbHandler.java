package project.pariamentApi;


import com.mongodb.client.MongoCollection;
import org.apache.uima.UIMAException;
import org.bson.Document;
import project.data.classes.PlenaryProtocol;
import project.database.MongoDBHandler;

public class ParliamentDbHandler {

    private MongoDBHandler dbHandler;

    public ParliamentDbHandler(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    private MongoCollection<Document> getCollection() {

        return dbHandler.getCollection("protocol");
    }

    public PlenaryProtocol addProtocol(PlenaryProtocol protocol) {
        try {
            dbHandler.insertProtocol(protocol);
            // todo update protocol with document
            return protocol;
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        }
    }

}
