package app.controllers;


import app.entities.BasketItem;
import app.entities.Orders;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    // main
    public static void index(Context ctx) {
        ctx.render("index.html");
    }

    public static void viewAllUsers(Context ctx) {
        ctx.render("viewAllUsers.html");
    }


    public static void yourOrders(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        OrderMapper orderMapper = new OrderMapper(connectionPool);
        OrderLineMapper orderLineMapper = new OrderLineMapper(connectionPool);

        List<Orders> userOrders = orderMapper.getOrdersByUserId(currentUser.getUserId());

        List<Map<String, Object>> pastOrders = new ArrayList<>();
        List<Map<String, Object>> currentOrders = new ArrayList<>();

        for (Orders order : userOrders) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getOrderId());
            orderData.put("pickup", order.getPickup());
            orderData.put("status", order.getStatus());
            orderData.put("lines", orderLineMapper.getOrderLineDescriptionsByOrderId(order.getOrderId()));

            if ("Completed".equalsIgnoreCase(order.getStatus())) {
                pastOrders.add(orderData);
            } else {
                currentOrders.add(orderData);
            }
        }

        ctx.attribute("currentUser", currentUser);
        ctx.attribute("pastOrders", pastOrders);
        ctx.attribute("currentOrders", currentOrders);

        ctx.render("YourOrders.html");
    }

    public static void adminProfile(Context ctx) {
        ctx.render("AdminUIProfile.html");
    }

    public static void menu(Context ctx) {
        ctx.render("menu.html");
    }




}