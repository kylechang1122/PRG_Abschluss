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

    public MongoCollection getCollection(){return this.collection;}

    public Document getUser(String id) {
        return null; // not finished yet
    }

}
