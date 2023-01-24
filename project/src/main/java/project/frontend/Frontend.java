package project.frontend;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class Frontend {

    public void initRoutes() throws IOException {

        // set external directory for the static files
        String staticDirectory = System.getProperty("user.dir") + File.separator + "web";
        staticFiles.externalLocation(staticDirectory);
        // set external template directory
        Configuration templateConfig = new Configuration(Configuration.getVersion());
        templateConfig.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + File.separator + "templates"));

        // set encoding to unicode
        templateConfig.setDefaultEncoding("utf-8");

        get("/editor", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "editor.ftl"));
        });
    }
}
