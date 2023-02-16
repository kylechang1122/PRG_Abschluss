package project.pariamentApi;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import project.database.MongoDBHandler;

public class ParliamentDbHandler {

    private MongoDBHandler dbHandler;

    public ParliamentDbHandler(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public MongoCollection<Document> getProtocolCollection() {

        return dbHandler.getCollection("protocol");
    }

    public MongoCollection<Document> getSpeakerCollection() {

        return dbHandler.getCollection("speaker");
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
        this.getProtocolCollection().insertOne(document);
        return this.getProtocol(id);
    }

    public void deletetProtocol(String id) {
        this.dbHandler.deleteProtocol(id);
    }

    public Document updateProtocol(Document document) {
        String id = document.getString("_id");
        if(id == null) {
            throw new IllegalArgumentException("cannot update protocol without id");
        }
        // _id is not allowed in updates
        document.remove("_id");
        Document query = new Document().append("_id",  id);
        Document updates = new Document().append("$set",  document);
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult result = this.getProtocolCollection().updateOne(query, updates, options);
        return this.getProtocol(id);
    }

    public boolean protocolExists(String id) {
        // create a filter by _id
        Bson filter = Filters.in("_id", id);
        // search the id in the collection
        FindIterable<Document> search = getProtocolCollection().find(filter);
        // check if the id exists in the collection
        return search.first() != null;
    }
    // speaker:
    public Document getSpeaker(String id) {
        return this.dbHandler.getObject(id, "speaker");
    }

    public Document createSpeaker(Document document) {
        String id = document.getString("_id");
        if(id == null) {
            id = new ObjectId().toString();
            document.append("_id", id);
        }
        this.getSpeakerCollection().insertOne(document);
        return this.getSpeaker(id);
    }

    public void deletetSpeaker(String id) {
        this.dbHandler.deleteSpeaker(id);
    }

    public Document updateSpeaker(Document document) {
        String id = document.getString("_id");
        if(id == null) {
            throw new IllegalArgumentException("cannot update speaker without id");
        }
        // _id is not allowed in updates
        document.remove("_id");
        Document query = new Document().append("_id",  id);
        Document updates = new Document().append("$set",  document);
        UpdateOptions options = new UpdateOptions().upsert(false);
        UpdateResult result = this.getSpeakerCollection().updateOne(query, updates, options);
        return this.getSpeaker(id);
    }

    public boolean speakerExists(String id) {
        // create a filter by _id
        Bson filter = Filters.in("_id", id);
        // search the id in the collection
        FindIterable<Document> search = getSpeakerCollection().find(filter);
        // check if the id exists in the collection
        return search.first() != null;
    }

}
