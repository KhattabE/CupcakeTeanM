package app.controllers;

import app.Main;
import app.entities.User;
import app.persistence.CupcakeMapper;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class CupcakeController {

    public static void buildYourCupcake(Context ctx) {

        User currentUser = ctx.sessionAttribute("currentUser");

        CupcakeMapper cupcakeMapper = new CupcakeMapper(Main.getConnectionPool());

        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", currentUser);
        model.put("toppings", cupcakeMapper.getAllToppings());
        model.put("bottoms", cupcakeMapper.getAllBottoms());
        model.put("message", ctx.consumeSessionAttribute("message"));

        ctx.render("buildYourCupcake.html", model);
    }
}
