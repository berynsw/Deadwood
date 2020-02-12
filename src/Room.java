import java.util.ArrayList;
import java.util.List;

public class Room {
    List<String> adjacents = new ArrayList<>();
    String name;
    boolean isSet;


    public Room(String name, List<String> adj, boolean set) {
        this.name = name;
        this.adjacents = adj;
        this.isSet = set;
    }

    public Room() {
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean isSet) {
        this.isSet = isSet;
    }

    public List<String> getAdjacents() {
        return adjacents;
    }

    public void addAdjacent(String adjacent) {
        this.adjacents.add(adjacent);
    }

    public void setAdjacents(List<String> adjacents){
        this.adjacents = adjacents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
