package app.controllers;


import app.entities.Orders;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
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
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            ctx.redirect("/profile");
            return;
        }

        UserMapper userMapper = new UserMapper(ConnectionPool.getInstance());
        List<User> users = userMapper.getAllUsers();

        ctx.attribute("currentUser", currentUser);
        ctx.attribute("users", users);
        ctx.render("adminViewAllUsers.html");
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
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            ctx.redirect("/profile");
            return;
        }

        ctx.attribute("currentUser", currentUser);
        ctx.render("AdminUIProfile.html");
    }

    public static void menu(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        ctx.attribute("currentUser", currentUser);
        ctx.render("menu.html");
    }

    public static void viewAllOrders(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        OrderMapper orderMapper = new OrderMapper(connectionPool);

        List<Orders> orders = orderMapper.getAllOrders();

        ctx.attribute("currentUser", currentUser);
        ctx.attribute("orders", orders);
        ctx.render("AdminViewAllOrders.html");
    }


    public static void deleteOrder(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            ctx.redirect("/profile");
            return;
        }

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        OrderMapper orderMapper = new OrderMapper(connectionPool);

        orderMapper.deleteOrderById(orderId);

        String userId = ctx.formParam("userId");

        if (userId != null) {
            ctx.redirect("/adminviewspecificuser/" + userId);
        } else {
            ctx.redirect("/view-all-orders");
        }
    }


    public static void viewSpecificUser(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/signin");
            return;
        }

        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            ctx.redirect("/profile");
            return;
        }

        int userId = Integer.parseInt(ctx.pathParam("id"));

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        UserMapper userMapper = new UserMapper(connectionPool);
        OrderMapper orderMapper = new OrderMapper(connectionPool);

        User selectedUser = userMapper.getUserById(userId);
        List<Orders> orders = orderMapper.getOrdersByUserId(userId);

        ctx.attribute("currentUser", currentUser);
        ctx.attribute("selectedUser", selectedUser);
        ctx.attribute("orders", orders);

        ctx.render("AdminViewSpecificUser.html");
    }
}