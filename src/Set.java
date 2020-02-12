import java.util.*;

public class Set extends Room{
    int maxShots;
    int currentShots;
    List<Role> roles = new ArrayList<>();
    Card card;

    public Set(String name, int shots, List<Role> roles, List<String> adjacents, boolean set){
        this.name = name;
        this.maxShots = shots;
        this.currentShots = maxShots;
        this.roles = roles;
        this.adjacents = adjacents;
        this.isSet = set;
    }

    public int getMaxShots() {
        return maxShots;
    }

    public void setMaxShots(int maxShots) {
        this.maxShots = maxShots;
    }

    public int getCurrentShots() {
        return currentShots;
    }

    public void setCurrentShots(int currentShots) {
        this.currentShots = currentShots;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
