package project.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoDBHandler {

    MongoClient dbClient = null;
    MongoDatabase db = null;

    MongoCollection<Document> collection = null;
    public MongoDBHandler(MongoDBConfig config){
        dbClient = new MongoClient(config.getServerAddress(),config.getCredentials(),config.getDefaultClientOptions());
        db = dbClient.getDatabase(config.getDatabase());
        collection = db.getCollection(config.getCollection());
    }

    public MongoCollection<Document> getCollection(){return this.collection;}

    public MongoCollection<Document> getCollection(String collection){return this.db.getCollection(collection);}

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
