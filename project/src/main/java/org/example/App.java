package org.example;

import org.apache.uima.UIMAException;
import org.bson.Document;
import org.xml.sax.SAXException;
import project.data.classes.*;
import project.database.*;
import project.frontend.Frontend;
import project.nlp.Engine;
import project.pariamentApi.ParliamentApi;
import project.userApi.UserDbHandler;
import project.userApi.UserApi;
import project.userApi.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    private MongoDBHandler dbConnection;

    static public String configDir() {
        URL resource = App.class.getResource("/dbconnectionconfig.txt");
        return resource.getPath();
    }

    public static void main(String[] args) {
        App app = new App();
        try{
            if (args.length > 0) {
                String arg = args[0];
                if (arg.equals("import")) {
                    app.importXML();
                } else if (arg.equals("analyze")) {
                    app.getAndAnalyseSpeech();
                }
            } else{
                startWebApp(app);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void startWebApp(App app) throws IOException {
            // set external directory for the static files
            String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
            staticFiles.externalLocation(staticDirectory);
            app.initApis();
            app.initFrontend();
    }

    App() {
        MongoDBConfig config = null;
        try {
            config = new MongoDBConfig(configDir());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dbConnection = new MongoDBHandler(config);
    }

    private void initApis() throws IOException {

        UserService userService = new UserService(new UserDbHandler(dbConnection));

        new UserApi(userService).initApi();
        new ParliamentApi(userService, dbConnection).initApi();
    }

    private void initFrontend() throws IOException {
        new Frontend().initRoutes();
    }

    private void importXML() {
        ParliamentFactory parliamentFactory = ParliamentFactory.getInstance();
        System.out.println(parliamentFactory.getProtocols().size());

        for (PlenaryProtocol protocol : parliamentFactory.getProtocols()) {
            System.out.println(protocol.getId());
            try {
                dbConnection.update(protocol);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Problem beim speichern von Protokoll: " + protocol.getId());
            }
        }
        for (Speaker speaker : parliamentFactory.getSpeakers()) {
            System.out.println(speaker.getName());
            try {
                dbConnection.update(speaker);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Problem beim speichern von Speaker: " + speaker.getId());
            }
        }
        System.out.println("XML import abgeschlossen");
    }

    /**
     * Method that retrieves the Speech from each protocol and perform analysis.
     *
     * @throws UIMAException Exception
     * @throws IOException   Exception
     * @throws SAXException  Exception
     */
    private void getAndAnalyseSpeech() throws UIMAException, IOException, SAXException {
        ParliamentFactory parliamentFactory = ParliamentFactory.getInstance();
        System.out.println(parliamentFactory.getProtocols().size());
        List<PlenaryProtocol> plenaryProtocols = parliamentFactory.getProtocols();

        Engine engine = new Engine();
        engine.createEngine();

        StringBuilder builder = new StringBuilder();
        String speechID = "";

        for (PlenaryProtocol plenaryProtocol : plenaryProtocols) {
            List<Speech> speeches = plenaryProtocol.getSpeeches();

            for (Speech speech : speeches) {
                List<Text> texts = speech.getTexts();
                speechID = speech.getId();

                for (Text text : texts) {
                    builder.append(text).append(System.lineSeparator());
                }

                Document document = null;
                try {
                    document = engine.analyse(speechID, builder.toString());
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                try {
                    dbConnection.insertNlpSpeech(document);
                } catch (Exception e) {
                    System.out.println("Cannot insert speech with id --> " + speechID);
                }

            }

        }
    }
}
