public class Role {
    String name;
    int rank;
    boolean filled;
    boolean onCard;

    public Role(String name, int rank, boolean onCard){
        this.name = name;
        this.rank = rank;
        this.onCard = onCard;
        this.filled = false;
    }
}
