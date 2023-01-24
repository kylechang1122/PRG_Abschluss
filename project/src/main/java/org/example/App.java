package org.example;

import project.database.*;
import project.exception.*;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;
import org.bson.Document;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App
{
    static public String configDir() {
        URL resource = App.class.getClassLoader().getResource("dbconnectionconfig.txt");
        return resource.getPath();
    }
    public static void main( String[] args ) {

        try {
            MongoDBConfig config = new MongoDBConfig(configDir());
            MongoDBHandler dbConnection = new MongoDBHandler(config);
            System.out.println("Hello World");
        }catch (DataBaseException ex){
            ex.printStackTrace();
        }
        routes();
    }

    static public void routes() {
        try {
            // set external directory for the static files
            String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
            staticFiles.externalLocation(staticDirectory);
            // set external template directory
            Configuration templateConfig = new Configuration(Configuration.getVersion());
            templateConfig.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + File.separator + "templates"));

            // set encoding to unicode
            templateConfig.setDefaultEncoding("utf-8");

            get("/editor", (request, response) -> {
                return  new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "editor.ftl"));
            });
            hasManagerRights(templateConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void hasManagerRights(freemarker.template.Configuration templateConfig) {
        try {

            before((request, response) -> {

                // 1) backend gets username and password from request Authentication header
                BasicAuthFilter basicAuthFilter = new BasicAuthFilter();
                String encodedHeader = request.headers("Authorization");
                if(encodedHeader == null) {
                    halt(401, "Not Authenticated");
                }
                encodedHeader = encodedHeader.substring(encodedHeader.lastIndexOf("Basic") + 1);
                String[] credentials = basicAuthFilter.extractCredentials(encodedHeader);
                // 2) backend gets user from database

                MongoDBConfig config = new MongoDBConfig(configDir());
                MongoDBHandler dbConnection = new MongoDBHandler(config);
                Document user = dbConnection.getUser(credentials[0]);
                // if exists
                if (user != null) {
                    //4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
                    String group = user.get("group", String.class);
                    if (user.get("password", String.class).equals(credentials[1])){
                        if (!group.equals("manager") && !group.equals("admin")) {
                            halt(405, "Not Authorized");
                        }
                    }
                    else {
                        halt(401, "Not Authenticate");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
