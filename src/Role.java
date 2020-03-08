import javax.swing.*;

public class Role {
    String name;
    int rank;
    boolean filled;
    boolean onCard;
    int x;
    int y;
    JButton button;
    JLabel label;



    public Role(String name, int rank, boolean onCard, int x, int y){
        this.name = name;
        this.rank = rank;
        this.onCard = onCard;
        this.filled = false;
        this.x = x;
        this.y = y;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isOnCard() {
        return onCard;
    }

    public void setOnCard(boolean onCard) {
        this.onCard = onCard;
    }


}
