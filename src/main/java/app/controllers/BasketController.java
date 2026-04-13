package app.controllers;

import app.entities.BasketItem;
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
        String topping = ctx.formParam("topping");
        String bottom = ctx.formParam("bottom");
        int quantity = Integer.parseInt(ctx.formParam("quantity"));

        double unitPrice = getPriceFromChoice(topping, bottom);

        BasketItem item = new BasketItem(topping, bottom, quantity, unitPrice);

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

    private static double getPriceFromChoice(String topping, String bottom) {
        double toppingPrice = switch (topping) {
            case "Chocolate" -> 15;
            case "Strawberry" -> 55;
            case "Vanilla" -> 10;
            case "Pistachio" -> 17;
            case "Almond" -> 40;
            default -> 0;
        };

        double bottomPrice = switch (bottom) {
            case "Chocolate" -> 10;
            case "Strawberry" -> 45;
            case "Vanilla" -> 20;
            case "Pistachio" -> 10;
            case "Almond" -> 50;
            default -> 0;
        };

        return toppingPrice + bottomPrice;
    }
}