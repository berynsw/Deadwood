import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Room {
    List<String> adjacents = new ArrayList<>();
    String name;
    boolean isSet;
    int x;
    int y;
    int h;
    int w;
    int playerCount;
    JButton button;
    List<Slot> slots = new ArrayList<>();

    public Room(String name, List<String> adj, int x, int y, int h, int w) {
        this.name = name;
        this.adjacents = adj;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.playerCount = 0;
    }

    public Room() {
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
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
