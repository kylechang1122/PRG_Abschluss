package project.pariamentApi;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.apache.uima.UIMAException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import project.database.MongoDBHandler;

public class ParliamentDbHandler {

    private MongoDBHandler dbHandler;

    public ParliamentDbHandler(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public MongoCollection<Document> getCollection() {

        return dbHandler.getCollection("protocol");
    }

    public Document getProtocol(String id) {
        return this.dbHandler.getObject(id, "protocol");
    }

    public Document createProtocol(Document document) {
        String id = document.getString("_id");
        if(id == null) {
            id = new ObjectId().toString();
            document.append("_id", id);
        }
        this.getCollection().insertOne(document);
        return this.getProtocol(id);
    }

    public void deletetProtocol(String id) {
        this.dbHandler.deleteProtocol(id);
    }

    public Document updateProtocol(Document updates) {
        String id = updates.getString("_id");
        if(id == null) {
            throw new IllegalArgumentException("cannot update protocol without id");
        }
        Document query = new Document().append("_id",  id);
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult result = this.getCollection().updateOne(query, updates, options);
        return this.getProtocol(id);
    }

    public boolean protocolExists(String id) {
        // create a filter by _id
        Bson filter = Filters.in("_id", id);
        // search the id in the collection
        FindIterable<Document> search = getCollection().find(filter);
        // check if the id exists in the collection
        return search.first() != null;
    }


}
