package org.example;

import org.apache.uima.UIMAException;
import org.bson.Document;
import org.xml.sax.SAXException;
import project.data.classes.ParliamentFactory;
import project.data.classes.PlenaryProtocol;
import project.data.classes.Speech;
import project.data.classes.Text;
import project.database.*;
import project.exception.DataBaseException;
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
 *
 */
public class App
{
    private static ParliamentFactory parliamentFactory;
    static public String configDir() {
        URL resource = App.class.getResource("/dbconnectionconfig.txt");
        return resource.getPath();
    }
    public static void main( String[] args ) {
        MongoDBConfig config = null;
        try {
            config = new MongoDBConfig(configDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MongoDBHandler dbConnection = new MongoDBHandler(config);
        if (args.length > 2 && args[0].equals("analyze")) {
            try {
                getAndAnalyseSpeech(dbConnection);
            } catch (IOException | UIMAException | SAXException e) {
                e.printStackTrace();
            }
        }
        try {
            // set external directory for the static files
            String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
            staticFiles.externalLocation(staticDirectory);
            initApis(dbConnection);
            initFrontend();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initApis(MongoDBHandler dbConnection) throws IOException {
        UserService userService = new UserService(new UserDbHandler(dbConnection));

        new UserApi(userService).initApi();
        new ParliamentApi(userService, dbConnection).initApi();
    }

    private static void initFrontend() throws IOException {
        new Frontend().initRoutes();
    }

    /**
     * Method that retrieves the Speech from each protocol and perform analysis.
     *
     * @param handler MongoDBHandler
     * @throws UIMAException Exception
     * @throws IOException   Exception
     * @throws SAXException  Exception
     */
    private static void getAndAnalyseSpeech(MongoDBHandler handler) throws UIMAException, IOException, SAXException {
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
                    handler.insertSpeech(document);
                } catch (Exception e) {
                    System.out.println("Cannot insert speech with id --> " + speechID);
                }

            }

        }
    }

}
