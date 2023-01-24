package project.userManagement;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import project.database.MongoDBHandler;

import static com.mongodb.client.model.Filters.eq;

public class UserDbHandler {

    private MongoDBHandler dbHandler;

    public UserDbHandler(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    private MongoCollection<Document> getCollection() {

        return dbHandler.getCollection("users");
    }

    public Document getUser(String id) {
        return getCollection().find(eq("_id", id)).first();
    }

    public void addUser(Document user) {
        // not finished yet
    }

    public void replaceUser(Document user) {
        // not finished yet
    }

    public void deleteUser(String id) {
        // not finished yet
    }

    public boolean userExists(String id) {
        return false; // not finished yet
    }

}
