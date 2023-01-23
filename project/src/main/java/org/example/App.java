package org.example;

import project.database.MongoDBConfig;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

/**
 * Hello world!
 *
 */
public class App
{
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
            // JSON transformer
            Gson gson = new Gson();
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
                // 1) backend gets username and password from request Authentication header

                // 2) backend gets user from database
                // if exists
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
