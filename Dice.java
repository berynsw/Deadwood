public class Dice {

    private int value = getDiceVal();

    //constructor
    public Dice() {
    }

    private int rollDice() {
        return -1; // DUMMY VALUE
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
