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
        // set external template directory
        Configuration templateConfig = new Configuration(Configuration.getVersion());
        templateConfig.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + File.separator + "templates"));

        // set encoding to unicode
        templateConfig.setDefaultEncoding("utf-8");
        // route of the home page
        get("/", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "home.ftl"));
        });
        // route of the login page
        get("/login", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "login.ftl"));
        });
        // route of the editor frontend
        get("/editor", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "editor.ftl"));
        });
        get("/editor/users", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "user-editor.ftl"));
        });
        get("/editor/protocols", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "protocol-overview.ftl"));
        });
        get("/editor/speakers", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "speaker-overview.ftl"));
        });
        get("/editor/speaker", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "speaker-editor.ftl"));
        });
        get("/editor/protocol", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "protocol-editor.ftl"));
        });
        get("/editor/agenda-item", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "agenda-item-editor.ftl"));
        });
        get("/editor/speech", (request, response) -> {
            return new FreeMarkerEngine(templateConfig).render(new ModelAndView(new HashMap<>(), "speech-editor.ftl"));
        });
    }
}
