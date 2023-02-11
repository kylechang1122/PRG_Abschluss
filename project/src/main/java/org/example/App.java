package org.example;

import project.database.*;
import project.frontend.Frontend;
import project.pariamentApi.ParliamentApi;
import project.userApi.UserDbHandler;
import project.userApi.UserApi;
import project.userApi.UserService;

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
            initApis();
            initFrontend();

        } catch (IOException e) {
            e.printStackTrace();
        }
        routes();
    }

    private static void initApis() throws IOException {
        MongoDBConfig config = new MongoDBConfig(configDir());
        MongoDBHandler dbConnection = new MongoDBHandler(config);
        UserService userService = new UserService(new UserDbHandler(dbConnection));

        new UserApi(userService).initApi();
        new ParliamentApi(userService).initApi();
    }

    private static void initFrontend() throws IOException {
        new Frontend().initRoutes();
    }


}
