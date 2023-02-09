package org.example;

import project.database.*;
import project.exception.*;
import project.frontend.Frontend;
import project.userManagement.UserDbHandler;
import project.userManagement.UserManagement;
import project.userManagement.UserService;

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
            MongoDBConfig config = new MongoDBConfig(configDir());
            MongoDBHandler dbConnection = new MongoDBHandler(config);
            System.out.println("Hello World");
            new Frontend().initRoutes();
            new UserManagement(new UserService(new UserDbHandler(dbConnection))).initApi();

        }catch (DataBaseException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        routes();
    }





}
