import java.util.ArrayList;
import java.util.List;

public class Room {
    List<Room> adjacents = new ArrayList<>();
    String name;



    public Room(String name) {
        this.name = name;
    }

    public Room() {
    }

    public List<Room> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(List<Room> adjacents) {
        this.adjacents = adjacents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
