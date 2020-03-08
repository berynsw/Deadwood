import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;

public class Set extends Room{
    List<Role> roles = new ArrayList<>();
    List<Shot> shotList = new ArrayList<>();

    int maxShots;
    int currentShots;
    Card card;
    JLabel cardIcon;
    boolean cardFlipped;

    public boolean isCardFlipped() {
        return cardFlipped;
    }

    public void setCardFlipped(boolean cardFlipped) {
        this.cardFlipped = cardFlipped;
    }

    public JLabel getCardIcon() {
        return cardIcon;
    }

    public void setCardIcon(JLabel cardIcon) {
        this.cardIcon = cardIcon;
    }

    public Set(String name, int shots, List<Role> roles, List<String> adjacents, int x, int y, int h, int w, List<Shot> shotList){
        this.name = name;
        this.maxShots = shots;
        this.currentShots = maxShots;
        this.shotList = shotList;
        this.roles = roles;
        this.adjacents = adjacents;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.cardIcon = null;
        this.cardFlipped = false;
    }

    public List<Shot> getShotList() {
        return shotList;
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
