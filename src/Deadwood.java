import java.util.*;
import org.w3c.dom.Document;

import java.awt.Color;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;

public class Deadwood {


    private int cardsOnBoard = 10;
    private int days = 1;
    private int maxDays = 4;
    private List<Player> players = new ArrayList<>();
    private HashMap<String,Set> sets = new HashMap<>();
    private Stack<Card> deck = new Stack<>();

    private Room trailer = null;
    private Office office = null;
    private Player currentPlayer;
    private int playerIndex = 0;
    private int budget = 0;

    public static Board board;

    private static Deadwood deadwood = new Deadwood();
    public static Deadwood getInstance(){
        return deadwood;
    }
    private Deadwood() {}
    public int getCardsOnBoard() {
        return this.cardsOnBoard;
    }
    public void setCardsOnBoard(int cardsOnBoard) {
        this.cardsOnBoard = cardsOnBoard;
    }
    public List<Player> getPlayers() { return players; }
    public HashMap<String, Set> getSets() { return sets; }
    public Stack<Card> getDeck() { return deck; }
    public Room getTrailer() { return trailer; }
    public Office getOffice() { return office; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }
    public int getDays() { return days; }
    //public void setBudget(int budget) { this.budget = budget; }
    public int getBudget() { return budget; }

    public void setPlayerBudget(Player player) {
        deadwood.sets.forEach((name, set) -> {
            if (name.equalsIgnoreCase(player.getRoom())) {
                this.budget = set.getCard().getBudget();
            }
        });
    }

    public static void main(String[] args) throws ParserConfigurationException, InterruptedException {
        //populate rooms and cards from xml
        Document doc1;
        Document doc2;
        ParseXML parsing = new ParseXML();
        try{
            //parse rooms
            doc1 = parsing.getDocFromFile("board.xml");
            deadwood.sets = parsing.readSetData(doc1);
            deadwood.trailer = parsing.readTrailerData(doc1);
            deadwood.office = parsing.readOfficeData(doc1);

            //parse cards
            doc2 = parsing.getDocFromFile("cards.xml");
            parsing.readCardData(doc2, deadwood.deck);
        }
        catch(Exception e){
            System.out.println("Error = "+e);
        }

        board = new Board();
        board.setVisible(true);
        board.makeRoomSlots();
        addPlayers(board.getPlayerNum());
        deadwood.currentPlayer = deadwood.players.get(0);


        deadwood.initDay();
        board.createTurnButtons();
    }

    public static void endTurn(){
        board.popUpMessage(deadwood.currentPlayer.getName()+"'s turn ended.");
        deadwood.currentPlayer.setMoved(false);
        if(deadwood.playerIndex == deadwood.players.size()-1){
            deadwood.playerIndex = 0;
        }
        else{
            deadwood.playerIndex++;
        }
        deadwood.currentPlayer = deadwood.players.get(deadwood.playerIndex);

        board.removeTurnButtons();
        board.createTurnButtons();
    }



    public static void endScene(Set set){
        //remove players from roles in scene
        for (int j = 0; j < deadwood.players.size(); j++) {
            Player player = deadwood.players.get(j);
            if (set.getName().equals(player.getRoom())) {
                player.setRole(null);
                player.setRehearsalTokens(0);
            }
        }
        //remove card
        set.setCard(null);
        board.removeCard(set);
        deadwood.cardsOnBoard--;

        //end day
        if(deadwood.cardsOnBoard == 1){

            deadwood.days++;
            if(deadwood.days > deadwood.maxDays){
                board.endOfGameDialog();
                System.exit(0);
            }
            else{
                board.endOfDayDialog();
                //start new day
                deadwood.initDay();
            }
        }
    }

    // Adds players to the game
    //  Updates credits, rank, or number of days based on number of players.
    public static void addPlayers(int input) {
        int playerCount = input;
        //check valid number of players
        if (playerCount < 2 || playerCount > 8) {
            System.out.println("Number of players must be between 2-8.");
            System.exit(0);
        }


        //initialize player and starting conditions
        for (int i = 0; i < playerCount; i++) {
            String name = JOptionPane.showInputDialog("Name of player " + (i + 1));
            Player player = new Player(name);
            player.setIconList(board.getDice()[i]);

            String icon = "images/dice/" + player.getIconList()[0];
            player.setIcon(icon);
            if (playerCount <= 3) {
                deadwood.maxDays = 3;
            }
            else if (playerCount == 5) {
                player.setCredits(2);
            }
            else if (playerCount == 6) {
                player.setCredits(4);
            }
            else if (playerCount >= 7) {
                player.setRank(2);
                icon = "images/dice/" + player.getIconList()[1];
                player.setIcon(icon);
            }
            deadwood.players.add(player);
        }
    }

    // Puts the players in trailer, a card from the deck into every set, and resets cardOnBoard value
    public static void initDay(){
        //put all players in trailer
        for(Player player : deadwood.players){
            player.setRole(null);

            board.placePlayerInRoom(player,"trailer");
            //board.initPlayer(player);
        }
        //board.initPlayer(deadwood.getPlayers());
        //put a card and shots in every set
        deadwood.sets.forEach((name, set) -> {
            if(set.getCard() != null){
                board.removeCard(set);
            }
            set.setCard(deadwood.deck.pop());
            board.placeCard(set, "images/CardBack.jpg");
            set.setCardFlipped(false);
            set.setCurrentShots(set.getMaxShots());
            for(Role role : set.getRoles()){
                role.setFilled(false);
            }
            board.placeShots(set);
        });
        deadwood.cardsOnBoard = 10;
    }

    public static Set getCurrentSet(Player player) {
        HashMap<String, Set> sets = Deadwood.getInstance().getSets();
        return sets.get(player.getRoom());
    }

    // Returns the list of rooms adjacent to the current location
    //  cases for if the current room is trailer office or set
    public static List<String> getNeighbors(String room){
        if(room.equalsIgnoreCase("trailer")){
            return deadwood.trailer.getAdjacents();
        }
        else if(room.equalsIgnoreCase("office")){
            return deadwood.office.getAdjacents();
        }
        else if(deadwood.sets.containsKey(room)){
            return deadwood.sets.get(room).getAdjacents();
        }
        else{
            System.out.println("invalid room/location string in player");
            return null;
        }
    }

    // Prints upgrade options and parses users input to upgrade rank
    public static void upgrade() {
        Player player = deadwood.currentPlayer;
        Office office = deadwood.office;
        if(player.getRoom().equalsIgnoreCase("office")){
            int rank = 0;
            String[] ranks = {"Rank 2", "Rank 3", "Rank 4", "Rank 5", "Rank 6"};
            int ranki =  JOptionPane.showOptionDialog(null, "What rank would you like to upgrade to?", "Upgrade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, ranks, ranks[0]);
            rank = office.getRank()[ranki];
            if(rank > player.getRank()){
                String[] currencies = {"Dollars", "Credits"};
                int curri =  JOptionPane.showOptionDialog(null, "Would you like to pay with dollars or credits?", "Upgrade", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, currencies, currencies[0]);
                String input = currencies[curri];
                if(input.equalsIgnoreCase("dollars")){
                    int dollars = player.getDollars();
                    for(int i = 0; i < 5; i++){
                        if(rank == office.getRank()[i]){
                            if(dollars >= office.getDollarCost()[i]){
                                player.setDollars(player.getDollars() - office.getDollarCost()[i]);
                                player.setRank(rank);
                                player.setIcon("images/dice/"+player.getIconList()[ranki]);
                                board.removePlayer(player);
                                board.placePlayerInRoom(player, "office");
                                board.popUpMessage("You upgraded to rank "+player.getRank());
                            }
                            else{
                                board.popUpMessage("You don't have enough dollars for that!");
                            }
                        }
                    }
                }
                else if(input.equalsIgnoreCase("credits")){
                    int credits = player.getCredits();
                    for(int i = 0; i < 5; i++){
                        if(rank == office.getRank()[i]){
                            if(credits >= office.getCreditCost()[i]){
                                player.setCredits(player.getCredits() - office.getCreditCost()[i]);
                                player.setRank(rank);
                                player.setIcon("images/dice/"+player.getIconList()[rank-1]);
                                board.removePlayer(player);
                                board.placePlayerInRoom(player, "office");
                                board.popUpMessage("You upgraded to rank "+player.getRank());
                            }
                            else{
                                board.popUpMessage("You don't have enough credits for that!");
                            }
                        }
                    }
                }
            }
            else{
                board.popUpMessage("That's not higher than your current rank!");
            }
        }
        else{
            board.popUpMessage("You need to be in the casting office to upgrade.");
        }
    }

    // Pay out players when a scene has ended
    public static void payOut(List<Player> p, Set set) {
        //initialize an arraylist of players that are on the set
        List<Player> playersOnSet = new ArrayList<>();
        List<Player> playersOnCard = new ArrayList<>();
        List<Player> playersOffCard = new ArrayList<>();
        List<Role> roles = set.getCard().getRoles();
        List<Integer> dice = new ArrayList<>();
        Random r = new Random();
        //generate list of dice rolls equivalent to the budget of the movie
        int budget = set.getCard().getBudget();
        for (int i = 0; i < budget; i++) {
            dice.add(r.nextInt(6) + 1);
        }
        //sort dice arrayList
        Collections.sort(dice);
        //add all players on set into arraylist
        for (int j = 0; j < p.size(); j++) {
            if (set.getName().equals(p.get(j).getRoom())) {
                playersOnSet.add(p.get(j));
            }
        }
        //for all players on the set, add them to onCard or offCard arraylists
        for (Player player : playersOnSet) {
            if (player.getRole() != null) {
                if (player.getRole().isOnCard()) {
                    playersOnCard.add(player);
                } else {
                    playersOffCard.add(player);
                }
            }
        }
        //initialize static set of onCard roles
        List<Role> onCardRoles = new ArrayList<>();
        onCardRoles.addAll(roles);
        //only payout players if there is at least 1 player working onCard
        if (playersOnCard.size() > 0) {
            //offCard players receive dollars equal to the rank of their role
            for (Player player : playersOffCard) {
                int reward = player.getRole().getRank();
                player.setDollars(player.getDollars() + reward);
                board.popUpMessage(player.getName()+" received $"+reward);
            }
            //Player highestRole = playersOnCard.get(0);
            while (!dice.isEmpty()) {
                //reinitialize roles set if empty
                if (roles.isEmpty()) {
                    roles.addAll(onCardRoles);
                }
                Role highestRole = roles.remove(roles.size() - 1);
                int highestDice = dice.remove(dice.size() - 1);
                for (Player player : playersOnCard) {
                    if (player.getRole() == highestRole) {
                        player.setDollars(player.getDollars() + highestDice);
                        board.popUpMessage(player.getName()+" received $"+highestDice);
                    }
                }
            }
        }
    }
}
