package org.example;

import com.google.gson.Gson;
import freemarker.template.Template;
import org.apache.uima.UIMAException;
import org.xml.sax.SAXException;

import project.data.classes.ParliamentFactory;
import project.data.classes.PlenaryProtocol;
import project.data.classes.Speaker;
import project.database.MongoDBConfig;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;
import project.utils.RestHelper;
import project.utils.WebHelper;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;


public class App {

    private static ParliamentFactory parliamentFactory;

    public static void main(String[] args) throws Exception {

        //REST SERVICE
        RestHelper restApi = new RestHelper();
        restApi.setPort();
        Gson gson = new Gson();

        //MONGODB
        String dbDefaultPath = App.class.getClassLoader().getResource("dbconnectionconfig.txt").getPath();
        MongoDBConfig mongoDBConfig = new MongoDBConfig(dbDefaultPath);
        MongoDBHandler handler = new MongoDBHandler(mongoDBConfig);

        //Initialize DB only with args
        if (args.length > 0 && args[0].equals("initialize")) {
            parliamentFactory = ParliamentFactory.getInstance();

            System.out.println(parliamentFactory.getProtocols().size());

            for (PlenaryProtocol protocol : parliamentFactory.getProtocols()) {
                System.out.println(protocol.getId());
                try {
                    handler.insertProtocol(protocol);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Problem beim speichern von Protokoll: " + protocol.getId());
                }
            }
            for (Speaker speaker : parliamentFactory.getSpeakers()) {
                System.out.println(speaker.getName());
                try {
                    handler.insertSpeaker(speaker);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Problem beim speichern von Speaker: " + speaker.getId());
                }
            }

        }


        //REST Function to initialize DB
        /*
            post http://localhost:8080/initialize
         */
        post("/initialize", (req, res) -> {
            parliamentFactory = ParliamentFactory.getInstance();

            System.out.println(parliamentFactory.getProtocols().size());
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "done");

            for (PlenaryProtocol protocol : parliamentFactory.getProtocols()) {
                System.out.println(protocol.getId());
                try {
                    handler.insertProtocol(protocol);
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                    System.out.println("Problem beim speichern von Protokoll: " + protocol.getId());
                    result.put("Problem beim speichern von Protokoll: ", protocol.getId());

                }
            }
            for (Speaker speaker : parliamentFactory.getSpeakers()) {
                System.out.println(speaker.getName());
                try {
                    handler.insertSpeaker(speaker);
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                    System.out.println("Problem beim speichern von Speaker: " + speaker.getId());
                    result.put("Problem beim speichern von Speaker: ", speaker.getId());

                }
            }


            return gson.toJson(result);
        });


        //REST Function to get single protocol
        /*
            http://localhost:8080/protocol?id=1923
         */
        get("/protocol", (req, res) -> {
            String id = req.queryParams("id");

            return gson.toJson(handler.getObject(id, "protocol"));
        });

        //REST Function to get count of protocols in db
        /*
            http://localhost:8080/countprotocols
         */
        get("/countprotocols", (req, res) -> {
            return gson.toJson(handler.getCollection("protocol").count());
        });

        //REST Function to get count of protocols in db
        /*
            post http://localhost:8080/protocol?id=1923
         */
        post("/protocol", (req, res) -> {
            String id = req.queryParams("id");
            handler.deleteProtocol(id);
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "deleted");
            return gson.toJson(result);
        });


        //REST Function to get single protocol
        /*
            http://localhost:8080/speaker?id=1923
         */
        get("/speaker", (req, res) -> {
            String id = req.queryParams("id");

            return gson.toJson(handler.getObject(id, "speaker"));
        });


        //REST Function to get count of protocols in db
        /*
            http://localhost:8080/countspeaker
         */
        get("/countspeaker", (req, res) -> {
            return gson.toJson(handler.getCollection("speaker").count());
        });

        //REST Function to get count of protocols in db
        /*
            post http://localhost:8080/speaker?id=11000008
         */
        post("/speaker", (req, res) -> {
            String id = req.queryParams("id");
            handler.deleteSpeaker(id);
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "deleted");
            return gson.toJson(result);
        });
    }

}
