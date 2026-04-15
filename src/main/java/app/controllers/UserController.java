package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {

    public static void signUp(Context ctx) {
        ctx.render("signUp.html");
    }

    public static void handleSignUp(Context ctx) {
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String confirmPassword = ctx.formParam("confirmPassword");

        if (!password.equals(confirmPassword)) {
            ctx.result("Passwords do not match");
            return;
        }

        UserMapper userMapper = new UserMapper(ConnectionPool.getInstance());

        if (userMapper.getUserByEmail(email) != null) {
            ctx.result("User already exists");
            return;
        }

        User user = new User(0, firstName, lastName, email, password, 500.00, "user");
        userMapper.createUser(user);

        ctx.redirect("/signin");
    }

    public static void signIn(Context ctx) {
        ctx.render("signin.html");
    }

    public static void handleSignIn(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        UserMapper userMapper = new UserMapper(ConnectionPool.getInstance());
        User user = userMapper.validateLogin(email, password);

        if (user == null) {
            ctx.attribute("error", "Wrong email or password. Try again.");
            ctx.attribute("email", email);
            ctx.render("signin.html");
            return;
        }

        ctx.sessionAttribute("currentUser", user);
        ctx.redirect("/menu");
    }

    public static void profile(Context ctx) {
        User sessionUser = ctx.sessionAttribute("currentUser");

        if (sessionUser == null) {
            ctx.redirect("/signin");
            return;
        }

        UserMapper userMapper = new UserMapper(ConnectionPool.getInstance());
        User currentUser = userMapper.getUserById(sessionUser.getUserId());

        if (currentUser == null) {
            ctx.sessionAttribute("currentUser", null);
            ctx.redirect("/signin");
            return;
        }

        ctx.sessionAttribute("currentUser", currentUser);

        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            ctx.redirect("/admin-profile");
            return;
        }

        ctx.attribute("currentUser", currentUser);
        ctx.render("profile.html");
    }

    public static void logout(Context ctx) {
        ctx.sessionAttribute("currentUser", null);
        ctx.redirect("/signin");
    }
}


