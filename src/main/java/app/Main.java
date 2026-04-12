package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.MainController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool =
            ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(
                    handler -> handler.setSessionHandler(SessionConfig.sessionConfig())
            );
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));

            config.routes.get("/", MainController::index);

            config.routes.get("/signup", UserController::signUp);
            config.routes.post("/signup", UserController::handleSignUp);
            config.routes.get("/signin", UserController::signIn);

            config.routes.get("/view-all-users", MainController::viewAllUsers);
            config.routes.get("/build-cupcake", MainController::buildYourCupcake);
            config.routes.get("/basket", MainController::yourBasket);
            config.routes.get("/orders", MainController::yourOrders);
            config.routes.get("/admin-profile", MainController::adminProfile);
        }).start(7070);
    }
}