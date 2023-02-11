package project.database;

import com.mongodb.MongoClient;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.uima.UIMAException;
import org.bson.Document;
import org.bson.conversions.Bson;
import project.data.classes.PlenaryProtocol;
import project.data.classes.Speaker;

import static com.mongodb.client.model.Filters.eq;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class MongoDBHandler {

    private MongoDBConfig pConfig = null;
    private MongoClient pClient = null;
    private MongoDatabase pDatabase = null;
    private MongoCollection<Document> pCollection = null;

    public MongoDBHandler(MongoDBConfig pConfig){
        this.pConfig = pConfig;
        init();
    }


    private void init(){

        // defind credentials (Username, database, password)
        List<MongoCredential> credentialList = new ArrayList(0);

        MongoCredential credential = MongoCredential.createScramSha1Credential(pConfig.getMongoUsername(), pConfig.getMongoDatabase(), pConfig.getMongoPassword().toCharArray());
        credentialList.add(credential);
        // defining Hostname and Port
        ServerAddress seed = new ServerAddress(pConfig.getMongoHostname(), pConfig.getMongoPort());
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        // defining some Options
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(20)
                .socketTimeout(300000)
                .maxWaitTime(300000)
                .socketKeepAlive(true)
                .serverSelectionTimeout(300000)
                .connectTimeout(300000)
                .sslEnabled(false)
                .build();

        // connect to MongoDB
        pClient = new MongoClient(seeds, credentialList, options);

        // select database
        pDatabase = pClient.getDatabase(pConfig.getMongoDatabase());

        // select default connection
        pCollection = pDatabase.getCollection(pConfig.getMongoCollection());

        // some debug information
        System.out.println("Connect to "+pConfig.getMongoDatabase()+" on "+pConfig.getMongoHostname());

    }

    public MongoCollection getCollection(String sCollection){
        return this.pDatabase.getCollection(sCollection);
    }

    public Document getObject(String sID, String sCollection){

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", sID);

        FindIterable<Document> result = this.getCollection(sCollection).find(whereQuery);

        Document doc = null;

        MongoCursor<Document> it = result.iterator();

        while(it.hasNext()){
            doc = it.next();
        }

        return doc;

    }

    public Document insertProtocol(PlenaryProtocol plenaryProtocol) throws UIMAException {

        Document rDocument = null;

        rDocument = getObject(plenaryProtocol.getId(), "protocol");

        if(rDocument==null){

            Document insertObject = MongoHelper.toMongoDocument(plenaryProtocol);

            this.getCollection("protocol").insertOne(insertObject);
            rDocument = getObject(plenaryProtocol.getId(), "protocol");
        }

        return rDocument;
    }




    public boolean update(PlenaryProtocol plenaryProtocol) throws UIMAException {

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", plenaryProtocol.getId());
        UpdateResult uResult = null;
        Document mongoDocument = MongoHelper.toMongoDocument(plenaryProtocol);
        try {
            uResult = this.getCollection("protocol").replaceOne(whereQuery, mongoDocument);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(uResult==null){
            return false;
        }
        else{
            if(uResult.getMatchedCount()==0){
                this.getCollection("protocol").insertOne(mongoDocument);
            }
        }
        return uResult.getModifiedCount()>0;

    }

    public void deleteProtocol(String id){
        Bson query = eq("_id", id);

        DeleteResult result = this.getCollection("protocol").deleteOne(query);
        System.out.println("Deleted document: " + result.getDeletedCount());

    }

    public Document insertSpeaker(Speaker pSpeaker)  {

        Document rDocument = null;

        rDocument = getObject(pSpeaker.getId(), "speaker");

        if(rDocument==null){

            Document insertObject = MongoHelper.toMongoDocument(pSpeaker);

            this.getCollection("speaker").insertOne(insertObject);
            rDocument = getObject(pSpeaker.getId(), "speaker");
        }
        else{
            Document updateObject = MongoHelper.toMongoDocument(pSpeaker);
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("_id", pSpeaker.getId());
            this.getCollection("speaker").updateOne(whereQuery, updateObject);
        }


        return rDocument;
    }

    public boolean update(Speaker pSpeaker) throws UIMAException {

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", pSpeaker.getId());
        UpdateResult uResult = null;
        Document mongoDocument = MongoHelper.toMongoDocument(pSpeaker);
        try {
            uResult = this.getCollection("speaker").replaceOne(whereQuery, mongoDocument);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(uResult==null){
            return false;
        }
        else{
            if(uResult.getMatchedCount()==0){
                this.getCollection("speaker").insertOne(mongoDocument);
            }
        }
        return uResult.getModifiedCount()>0;

    }

    public void deleteSpeaker(String id){
        Bson query = eq("_id", id);

        DeleteResult result = this.getCollection("speaker").deleteOne(query);
        System.out.println("Deleted speaker: " + result.getDeletedCount());

    }

}
