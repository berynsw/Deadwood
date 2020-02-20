import java.util.Random;

public class Dice {

    private int value = getDiceVal();

    //constructor
    public Dice() {
    }

    private int rollDice() {
        Random r = new Random();
        return r.nextInt(6) + 1;
    }

    private int getDiceVal() {
        return rollDice();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
