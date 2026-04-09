package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import app.persistence.ConnectionPool;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "lifehack";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin javApp = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7070);


    }
}
