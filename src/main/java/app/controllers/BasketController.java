package app.controllers;

import app.entities.BasketItem;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.entities.OrderLine;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BasketController {

    public static void addToBasket(Context ctx) {
        String toppingIdParam = ctx.formParam("topping");
        String bottomIdParam = ctx.formParam("bottom");
        String quantityParam = ctx.formParam("quantity");

        if (toppingIdParam == null || bottomIdParam == null || quantityParam == null) {
            ctx.status(400).result("Missing form data");
            return;
        }

        int toppingId = Integer.parseInt(toppingIdParam);
        int bottomId = Integer.parseInt(bottomIdParam);
        int quantity = Integer.parseInt(quantityParam);

        if (quantity < 1) {
            quantity = 1;
        }

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        CupcakeMapper cupcakeMapper = new CupcakeMapper(connectionPool);

        CupcakeTop toppingChoice = cupcakeMapper.getToppingById(toppingId);
        CupcakeBottom bottomChoice = cupcakeMapper.getBottomById(bottomId);

        if (toppingChoice == null || bottomChoice == null) {
            ctx.status(400).result("Invalid cupcake selection");
            return;
        }

        double unitPrice = toppingChoice.getPrice() + bottomChoice.getPrice();

        BasketItem item = new BasketItem(
                toppingChoice.getTopping(),
                bottomChoice.getBottom(),
                quantity,
                unitPrice
        );

        List<BasketItem> basket = ctx.sessionAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        basket.add(item);
        ctx.sessionAttribute("basket", basket);

        ctx.redirect("/basket");
    }

    public static void yourBasket(Context ctx) {
        List<BasketItem> basket = ctx.sessionAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        double total = 0;
        for (BasketItem item : basket) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        User currentUser = ctx.sessionAttribute("currentUser");

        ctx.attribute("basket", basket);
        ctx.attribute("total", total);
        ctx.attribute("currentUser", currentUser);
        ctx.render("YourBasket.html");
    }

    public static void increaseBasketItem(Context ctx) {
        int index = Integer.parseInt(ctx.formParam("index"));
        List<BasketItem> basket = ctx.sessionAttribute("basket");

        if (basket != null && index >= 0 && index < basket.size()) {
            BasketItem item = basket.get(index);
            item.setQuantity(item.getQuantity() + 1);
            ctx.sessionAttribute("basket", basket);
        }

        ctx.redirect("/basket");
    }

    public static void decreaseBasketItem(Context ctx) {
        int index = Integer.parseInt(ctx.formParam("index"));
        List<BasketItem> basket = ctx.sessionAttribute("basket");

        if (basket != null && index >= 0 && index < basket.size()) {
            BasketItem item = basket.get(index);
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
            ctx.sessionAttribute("basket", basket);
        }

        ctx.redirect("/basket");
    }

    public static void removeBasketItem(Context ctx) {
        int index = Integer.parseInt(ctx.formParam("index"));
        List<BasketItem> basket = ctx.sessionAttribute("basket");

        if (basket != null && index >= 0 && index < basket.size()) {
            basket.remove(index);
            ctx.sessionAttribute("basket", basket);
        }

        ctx.redirect("/basket");
    }

    public static void confirmBasket(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");
        List<BasketItem> basket = ctx.sessionAttribute("basket");

        if (currentUser == null) {
            ctx.result("You must be signed in");
            return;
        }

        if (basket == null || basket.isEmpty()) {
            ctx.result("Basket is empty");
            return;
        }

        double total = 0;
        for (BasketItem item : basket) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        if (currentUser.getBalance() < total) {
            ctx.result("Not enough balance");
            return;
        }

        String pickupDate = ctx.formParam("pickupDate");

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        OrderMapper orderMapper = new OrderMapper(connectionPool);
        OrderLineMapper orderLineMapper = new OrderLineMapper(connectionPool);
        CupcakeMapper cupcakeMapper = new CupcakeMapper(connectionPool);
        UserMapper userMapper = new UserMapper(connectionPool);

        int orderId = orderMapper.createOrder(
                currentUser.getUserId(),
                "In process",
                LocalDate.now(),
                total,
                pickupDate
        );

        for (BasketItem item : basket) {
            int cupcakeId = cupcakeMapper.getOrCreateCupcakeId(item.getBottom(), item.getTopping());
            double lineTotal = item.getQuantity() * item.getUnitPrice();

            OrderLine orderLine = new OrderLine(
                    0,
                    orderId,
                    cupcakeId,
                    item.getQuantity(),
                    item.getUnitPrice(),
                    lineTotal
            );

            orderLineMapper.addOrderLine(orderLine);
        }

        double newBalance = currentUser.getBalance() - total;
        userMapper.updateBalance(currentUser.getUserId(), newBalance);
        currentUser.setBalance(newBalance);
        ctx.sessionAttribute("currentUser", currentUser);

        ctx.sessionAttribute("basket", new ArrayList<BasketItem>());

        ctx.redirect("/orders");
    }
}