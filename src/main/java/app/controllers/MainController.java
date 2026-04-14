package app.controllers;


import app.entities.BasketItem;
import app.entities.User;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    // main
    public static void index(Context ctx) {
        ctx.render("index.html");
    }

    public static void viewAllUsers(Context ctx) {
        ctx.render("viewAllUsers.html");
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