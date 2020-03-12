import javax.swing.*;
import java.util.*;

public class Player {
    private String name;
    private int rank;
    private int credits;
    private int dollars;
    private int rehearsalTokens;
    private String room;
    private Role role;
    private String icon;
    private JLabel label;
    private boolean moved;
    private int x;
    private int y;
    private String[] iconList;


    private boolean onCard;
    private boolean turn;

    public String[] getIconList() {
        return iconList;
    }

    public void setIconList(String[] iconList) {
        this.iconList = iconList;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }
    public boolean isTurn() { return turn; }

    public void setTurn(boolean turn) { this.turn = turn; }

    public boolean isOnCard() {
        return onCard;
    }

    public void setOnCard(boolean onCard) {
        this.onCard = onCard;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) { this.icon = icon; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    //constructor
    public Player(String name) {
        this.name = name;
        this.rank = 1;
        this.rehearsalTokens = 0;
        this.turn = false;
        this.x = 0;
        this.y = 0;

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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public int getRehearsalTokens() {
        return rehearsalTokens;
    }

    public void setRehearsalTokens(int rehearsalTokens) {
        this.rehearsalTokens = rehearsalTokens;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Attempts to act in player's current role
    public static boolean act(Player player, Set set, List<Player> players) {
        Deadwood deadwood = Deadwood.getInstance();
        if (player.getRole()!= null) {
            Random r = new Random();
            int roll = r.nextInt(6) + 1;
            int budget = set.getCard().getBudget();

            System.out.printf("You need to roll a %d...\n", budget);
            System.out.printf("You rolled a %d and you have %d rehearsal chips!\n", roll, player.getRehearsalTokens());

            //if you fail acting
            if (budget > (roll + player.getRehearsalTokens())) {
                System.out.println("Apparently, you're not as good an actor as you think.");
                deadwood.board.popUpMessage("You suck at acting! Give up on your dreams kid.");
                //if failed off-card, still get a dollar
                if (!player.getRole().isOnCard()) {
                    player.setDollars(player.getDollars() + 1);
                    System.out.println("As compensation, you get 1 dollar.");
                    deadwood.board.popUpMessage(player.getName()+" received $1");
                } else {
                    System.out.println("As compensation, you get nothing. Better luck next time!");
                }

                return false;
            }
            //if you succeed acting
            else {
                System.out.println("It appears you're not as bad an actor as you look.");

                if (player.getRole().isOnCard()) {
                    player.setCredits(player.getCredits() + 2);
                    System.out.println("As compensation, you get 2 credits.");
                    deadwood.board.popUpMessage(player.getName()+" received 2cr");
                } else {
                    player.setCredits(player.getCredits() + 1);
                    player.setDollars(player.getDollars() + 1);
                    System.out.println("As compensations, you get 1 dollar and 1 credit.");
                    deadwood.board.popUpMessage(player.getName()+" received 1cr and $1");
                }
                //decrement shot counter
                set.setCurrentShots(set.getCurrentShots() - 1);
                deadwood.board.removeShot(set);

                if (set.getCurrentShots() >= 1) {
                    if (set.getCurrentShots() > 1) {
                        System.out.println("You remove one shot counter. There are " + set.getCurrentShots() + " shots left.");
                    } else {
                        System.out.println("You remove one shot counter. There is 1 shot left.");
                    }
                //scene is over
                } else {
                    System.out.println("That about wraps it up, the scene is over!");
                    deadwood.board.popUpMessage("That about wraps it up, the scene is over.");
                    deadwood.payOut(players, set);
                    deadwood.endScene(set);
                }
            }
            return true;
        } else {
            System.out.println("You are not currently acting in a role.");
            return true;
        }
    }

    // Display neighboring room, calls work() and upgrade() if appropriate
    public static boolean move(Player player, HashMap<String, Set> sets, Room trailer, Office office, Scanner scan){
        Deadwood deadwood = Deadwood.getInstance();
        if(player.getRole() != null){
            System.out.println("You are acting in a role and can't move. So, your only options are act, rehearse, or end.");
        }
        else{
            System.out.println("The neighboring rooms to your current room are:");
            List<String> neighbors = deadwood.getNeighbors(player.getRoom());
            for(String room : neighbors){
                System.out.println(" "+room);
            }

            System.out.println("Where would you like to move to?");
            String input = scan.nextLine().toLowerCase();

            //check if the move request is a neighboring room
            if(neighbors.contains(input)){
                player.setRoom(input);
                System.out.println("You successfully moved to "+input);
                //if it's a set
                if(sets.containsKey(input)){
                    work(player, sets.get(input), scan);
                }
                else if(input.equals("office")){
                    System.out.println("Since you moved to the office would you like to upgrade?");
                    //deadwood.upgrade();
                }
                return false;
            }
            else{
                System.out.println("Room is either invalid or not next to the room you're in. Try again.");
            }
        }
        return true;
    }

    // Wrapper function for obtaining a new role
    public static boolean work(Player player, Set set, Scanner scan){
        Deadwood deadwood = Deadwood.getInstance();
        if(!deadwood.getSets().containsKey(player.getRoom())){
            System.out.println("You're not in a set. You must move to a set to work.");
        }
        else{
            if(player.getRole() != null){
                System.out.println("You're already working. Options are act, rehearse, or end.");
            }
            else{
                //if the set is active
                if(set.getCard() != null){
                    List<Role> offCard = set.getRoles();
                    List<Role> onCard = set.getCard().getRoles();
                    //deadwood.displayRoles(offCard, onCard);
                    System.out.println("If you would like to work on one of these roles type the role 'name'.");
                    String input = scan.nextLine().toLowerCase();
                    return takeRole(player, onCard, offCard, input);
                }
                else{
                    System.out.println("This set is wrapped. You can't work here today.");
                }
            }
        }
        return true;
    }

    // Searches through list of roles for specified role name (on or off card) and allows player to take that role if possible
    public static boolean takeRole(Player player, List<Role> onCard, List<Role> offCard, String input){
        for(Role role : onCard){
            if(input.equalsIgnoreCase(role.getName())){
                if(player.getRank() >= role.getRank()){
                    if(!role.isFilled()){
                        player.setRole(role);
                        player.setOnCard(true);
                        role.setFilled(true);
                        System.out.printf("You took the on card role %s!%n", role.getName());
                        return false;
                    }
                    else{
                        System.out.printf("That role is already filled.%n");
                    }
                }
                else{
                    System.out.println("You're not high enough rank");
                }
            }
        }
        for(Role role : offCard){
            if(input.equalsIgnoreCase(role.getName())){
                if(player.getRank() >= role.getRank()){
                    if(!role.isFilled()){
                        player.setRole(role);
                        player.setOnCard(false);
                        role.setFilled(true);
                        System.out.printf("You took the off card role %s!%n", role.getName());
                        return false;
                    }
                    else{
                        System.out.printf("That role is already filled.%n");
                    }
                }
                else{
                    System.out.println("You're not high enough rank");
                }
            }
        }
        return true;
    }

    // Adds 1 to player's practice tokens if applicable
    public static boolean rehearse(Player player, HashMap<String, Set> sets){
        if(player.getRole() != null){
            int budget = sets.get(player.getRoom()).getCard().getBudget();
            if(player.getRehearsalTokens() < budget-1) {
                player.setRehearsalTokens(player.getRehearsalTokens() + 1);
                System.out.println("+1 practice chips!");
                return false;
            }
            else{
                System.out.println("You already have max rehearsal tokens for this cards budget. Try acting!");
            }
        }
        else{
            System.out.println("You aren't working.");
        }
        return true;
    }
    
}
