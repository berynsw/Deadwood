import java.util.List;

public class Office extends Room {
    private int[] rank = {2,3,4,5,6};
    private int[] dollarCost = {4,10,18,28,40};
    private int[] creditCost = {5,10,15,20,25};

    public Office(String name, List<String> adj) {
        this.name = name;
        this.adjacents = adj;
        this.x = 9;
        this.y = 459;
        this.h = 208;
        this.w = 209;
    }
    public int[] getRank() {
        return rank;
    }

    public void setRank(int[] rank) {
        this.rank = rank;
    }

    public int[] getDollarCost() {
        return dollarCost;
    }

    public void setDollarCost(int[] dollarCost) {
        this.dollarCost = dollarCost;
    }

    public int[] getCreditCost() {
        return creditCost;
    }

    public void setCreditCost(int[] creditCost) {
        this.creditCost = creditCost;
    }
}
