package org.example;

import project.database.*;
import project.exception.*;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;
import org.bson.Document;

import java.io.File;
import java.util.HashMap;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App
{
    static public String configDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "dbconnectionconfig.txt";
    public static void main( String[] args ) {

        try {
            MongoDBConfig config = new MongoDBConfig("C:\\Users\\Driton\\Desktop\\Fiverr Project\\parliamentreader\\project\\src\\main\\resources\\dbconnectionconfig.txt");
            MongoDBHandler dbConnection = new MongoDBHandler(config);
            System.out.println("Hello World");
        }catch (DataBaseException ex){
            ex.printStackTrace();
        }

    }

    static public void routes() {
        try {
            // set external directory for the static files
            String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
            staticFiles.externalLocation(staticDirectory);
            // set external template directory
            freemarker.template.Configuration templateConfig = new freemarker.template.Configuration(Configuration.getVersion());
            templateConfig.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + File.separator + "templates"));

            // set encoding to unicode
            templateConfig.setDefaultEncoding("utf-8");

            get("/", (request, response) -> {
                return  new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "login.ftl"));
            });
            hasManagerRights(templateConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void hasManagerRights(freemarker.template.Configuration templateConfig) {
        try {

            before((request, response) -> {

                // 1) backend gets username and password from request Authentication heade
                BasicAuthFilter basicAuthFilter = new BasicAuthFilter();
                String encodedHeader = request.headers("Authorization");
                encodedHeader.substring(encodedHeader.lastIndexOf("Basic") + 1);
                String[] credentials = basicAuthFilter.extractCredentials(encodedHeader);
                // 2) backend gets user from database

                MongoDBConfig config = new MongoDBConfig(configDir);
                MongoDBHandler dbConnection = new MongoDBHandler(config);
                Document user = dbConnection.getUser(credentials[0]);
                // if exists
                if (user != null) {
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
                //         -> 3)
                // if not
                // halt(401)
                // 3) backend compare the password
                // if match
                //         -> 4)
                // if not
                // halt(401)
                // 4) backend checks if the user belongs to the correct group with the required rights for the requested content (e.g. admin group for admin content)
                // if user has sufficient rights -> respond(200, requested data)
                // if not
                // halt(405)
            });

            get("/dashboard", (request, response) -> {
                return  new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "dashboard.ftl"));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
