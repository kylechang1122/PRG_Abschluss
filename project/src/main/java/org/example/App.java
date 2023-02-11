package org.example;

import project.database.*;
import project.frontend.Frontend;
import project.pariamentApi.ParliamentApi;
import project.userApi.UserDbHandler;
import project.userApi.UserApi;
import project.userApi.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App
{
    static public String configDir() {
        URL resource = App.class.getResource("/dbconnectionconfig.txt");
        return resource.getPath();
    }
    public static void main( String[] args ) {
        try {
            // set external directory for the static files
            String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
            staticFiles.externalLocation(staticDirectory);
            initApis();
            initFrontend();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initApis() throws IOException {
        MongoDBConfig config = new MongoDBConfig(configDir());
        MongoDBHandler dbConnection = new MongoDBHandler(config);
        UserService userService = new UserService(new UserDbHandler(dbConnection));

        new UserApi(userService).initApi();
        new ParliamentApi(userService, dbConnection).initApi();
    }

    private static void initFrontend() throws IOException {
        new Frontend().initRoutes();
    }


}
