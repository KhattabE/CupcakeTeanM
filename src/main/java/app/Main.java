package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
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

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

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
            config.routes.post("/signin", UserController::handleSignIn);

            config.routes.get("/view-all-users", MainController::viewAllUsers);
            config.routes.get("/build-cupcake", CupcakeController::buildYourCupcake);
            config.routes.get("/orders", MainController::yourOrders);
            config.routes.get("/admin-profile", MainController::adminProfile);
            config.routes.get("/profile", UserController::profile);
            config.routes.get("/logout", UserController::logout);
            config.routes.get("/menu", MainController::menu);

            config.routes.get("/basket", BasketController::yourBasket);
            config.routes.post("/basket/add", BasketController::addToBasket);
            config.routes.post("/basket/increase", BasketController::increaseBasketItem);
            config.routes.post("/basket/decrease", BasketController::decreaseBasketItem);
            config.routes.post("/basket/remove", BasketController::removeBasketItem);
            config.routes.post("/basket/confirm", BasketController::confirmBasket);
            config.routes.get("/view-all-orders", MainController::viewAllOrders);
            config.routes.post("/admin/delete-order", MainController::deleteOrder);
        }).start(7070);
    }
}