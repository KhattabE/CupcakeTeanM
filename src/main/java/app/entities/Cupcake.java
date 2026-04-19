package app.entities;

public class Cupcake {

    private int cupcakeId;
    private int cupcakeTopId;
    private int cupcakeBottomId;

    public Cupcake(int cupcakeId, int cupcakeTopId, int cupcakeBotomId) {
        this.cupcakeId = cupcakeId;
        this.cupcakeTopId = cupcakeTopId;
        this.cupcakeBottomId = cupcakeBotomId;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public int getCupcakeTopId() {
        return cupcakeTopId;
    }

    public int getCupcakeBottomId() {
        return cupcakeBottomId;
    }
}
