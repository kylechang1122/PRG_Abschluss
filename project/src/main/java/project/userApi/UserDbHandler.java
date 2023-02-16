package project.userApi;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import project.database.MongoDBHandler;

import static com.mongodb.client.model.Filters.eq;

public class UserDbHandler {

    private MongoDBHandler dbHandler;

    public UserDbHandler(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public MongoCollection<Document> getCollection() {

        return dbHandler.getCollection("users");
    }

    public Document getUser(String id) {
        return getCollection().find(eq("_id", id)).first();
    }

    public void addUser(Document user) {
        this.getCollection().insertOne(user);
    }

    public void replaceUser(Document document) {
        // get id from input document user
        String userId = document.getString("_id");
        // _id is not allowed in updates
        document.remove("_id");
        Document query = new Document().append("_id",  userId);
        Document updates = new Document().append("$set",  document);
        UpdateOptions options = new UpdateOptions().upsert(false);
        //the user is updated with the new Document
        UpdateResult result = this.getCollection().updateOne(query, updates, options);
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
