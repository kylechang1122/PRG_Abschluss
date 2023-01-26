package project.userManagement;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
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
        this.getCollection().insertOne(user);
    }

    public void replaceUser(Document user) {
        // get id from input document user
        String userId = user.getString("_id");
        // filter the field "_id" with userId
        Bson filter = Filters.in("_id", userId);
        //the user is updated with the new Document
        this.getCollection().updateOne(filter, user);
    }

    public void deleteUser(String id) {
        // create a filter by _id
        Bson filter = Filters.in("_id", id);
        // delete the document from DB
        this.getCollection().deleteOne(filter);

    }

    public boolean userExists(String id) {
        // create a filter by _id
        Bson filter = Filters.in("_id", id);
        // search the id in the collection
        FindIterable<Document> search = getCollection().find(filter);
        // check if the id exists in the collection
        return search.first() != null;
    }

}
