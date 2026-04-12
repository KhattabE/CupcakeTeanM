package app.controllers;


import io.javalin.http.Context;

public class MainController {

    // main
    public static void index(Context ctx) {
        ctx.render("index.html");
    }

    public static void viewAllUsers(Context ctx) {
        ctx.render("viewAllUsers.html");
    }

    public static void buildYourCupcake(Context ctx) {
        ctx.render("buildYourCupcake.html");
    }

    public static void yourBasket(Context ctx) {
        ctx.render("YourBasket.html");
    }

    public static void yourOrders(Context ctx) {
        ctx.render("YourOrders.html");
    }

    public static void adminProfile(Context ctx) {
        ctx.render("AdminUIProfile.html");
    }

    public static void menu(Context ctx) {
        ctx.render("menu.html");
    }
}