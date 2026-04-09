package app.entities;

public class Cupcake {

    private int cupcakeId;
    private int cupcakeTopId;
    private int cupcakeBotomId;

    public Cupcake(int cupcakeId, int cupcakeTopId, int cupcakeBotomId) {
        this.cupcakeId = cupcakeId;
        this.cupcakeTopId = cupcakeTopId;
        this.cupcakeBotomId = cupcakeBotomId;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public int getCupcakeTopId() {
        return cupcakeTopId;
    }

    public int getCupcakeBotomId() {
        return cupcakeBotomId;
    }

    public void setCupcakeId(int cupcakeId) {
        this.cupcakeId = cupcakeId;
    }

    public void setCupcakeTopId(int cupcakeTopId) {
        this.cupcakeTopId = cupcakeTopId;
    }

    public void setCupcakeBotomId(int cupcakeBotomId) {
        this.cupcakeBotomId = cupcakeBotomId;
    }
}
