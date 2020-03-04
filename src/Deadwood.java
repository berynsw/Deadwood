import java.util.*;
import org.w3c.dom.Document;

import java.awt.Color;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;

public class Deadwood {


    private int cardsOnBoard = 10;
    private int days = 4;
    private List<Player> players = new ArrayList<>();
    private HashMap<String,Set> sets = new HashMap<>();
    private Stack<Card> deck = new Stack<>();

    private Room trailer = null;
    private Office office = null;
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






    public static void main(String[] args) throws ParserConfigurationException {
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




        int num = board.getPlayerNum();
        addPlayers(num);

        while(deadwood.days > 0){
            initDay(deadwood.players, deadwood.sets, deadwood.deck);
            while(deadwood.cardsOnBoard > 1){
                for (int i = 0; i < deadwood.players.size(); i++) {
                    takeTurn(deadwood.players.get(i), deadwood.players, deadwood.sets, deadwood.trailer, deadwood.office);
                    if (deadwood.cardsOnBoard == 1) {            //checks if day is over during player rotation
                        break;
                    }
                }
            }
            deadwood.days--;
        }
        //displayTotalScores(deadwood.players);
    }



















    // Adds players to the game
    //  Updates credits, rank, or number of days based on number of players.
    public static void addPlayers(int input) {
        System.out.println(input);
        //get number of players from args
        int playerCount = input;


        //check valid number of players
        if (playerCount < 2 || playerCount > 8) {
            System.out.println("Number of players must be between 2-8.");
            System.exit(0);
        }
        //create scanner
        Scanner scan = new Scanner(System.in);

        //initialize player and starting conditions
        for (int i = 0; i < playerCount; i++) {
            System.out.println(board.getPlayerDice().get(i));
            deadwood.players.add(new Player("dummy", board.getPlayerDice().get(i)));
            if (playerCount <= 3) {
                deadwood.days = 3;
            } else if (playerCount == 5) {
                deadwood.players.get(i).setCredits(2);
            } else if (playerCount == 6) {
                deadwood.players.get(i).setCredits(4);
            } else if (playerCount >= 7) {
                deadwood.players.get(i).setRank(2);
            }
        }
        System.out.println();
    }

    // Puts the players in trailer, a card from the deck into every set, and resets cardOnBoard value
    public static void initDay(List<Player> players, HashMap<String,Set> sets, Stack<Card> deck){
        //put all players in trailer
        for(Player player : players){
            player.setRoom("trailer");
            player.setRole(null);
        }

        //put a card in every set
        sets.forEach((name, set) -> {
            set.setCard(deck.pop());
        });
        deadwood.cardsOnBoard = 10;
    }

    // Calls corresponding player action
    public static void takeTurn(Player player, List<Player> players, HashMap<String, Set> sets, Room trailer, Office office) {
        System.out.printf("%s it's your turn to play.%n", player.getName());
        Scanner scan = new Scanner(System.in);
        boolean turn = true;
        String input;

        while(turn){
            System.out.printf("The options are: 'active player', 'all players', 'move', 'work', 'rehearse', 'act', 'upgrade', and 'end'.%n");
            input = scan.nextLine().toLowerCase();
            switch(input){
                case "end":
                    turn = false;
                    break;
                case "act":
                    turn = player.act(player, sets.get(player.getRoom()), players);
                    break;
                case "move":
                    turn = player.move(player, sets, trailer, office, scan);
                    break;
                case "work":
                    turn = player.work(player, sets.get(player.getRoom()), scan);
                    break;
                case "rehearse":
                    turn = player.rehearse(player, sets);
                    break;
                case "upgrade":
                    upgrade(office, player, scan);
                    break;
                default:
                    System.out.println("Unrecognized input.");
            }
        }
        System.out.printf("%s's turn ended.%n%n",player.getName());
    }



    // Returns the list of rooms adjacent to the current location
    //  cases for if the current room is trailer office or set
    public static List<String> getNeighbors(String room, HashMap<String, Set> sets, Room trailer, Room office){
        if(room.equalsIgnoreCase("trailer")){
            return trailer.getAdjacents();
        }
        else if(room.equalsIgnoreCase("office")){
            return office.getAdjacents();
        }
        else if(sets.get(room) != null){
            return sets.get(room).getAdjacents();
        }
        else{
            System.out.println("invalid room/location string in player");
            return null;
        }
    }

    // Prints upgrade options and parses users input to upgrade rank
    public static void upgrade(Office office, Player player, Scanner scan) {
        if(player.getRoom().equalsIgnoreCase("office")){
            System.out.println("The upgrade options are:");
            for(int i = 0; i < 5; i++){
                System.out.printf(" rank %d: %d dollars, %d credits%n", office.getRank()[i], office.getDollarCost()[i], office.getCreditCost()[i]);
            }
            System.out.println("select a rank");
            String input = scan.nextLine();
            int rank = 0;
            try{
                rank = Integer.parseInt(input);
                if(rank > player.getRank()){
                    System.out.println("would you like to pay with 'dollars' or 'credits'?");
                    input = scan.nextLine();
                    if(input.equalsIgnoreCase("dollars")){
                        int dollars = player.getDollars();
                        for(int i = 0; i < 5; i++){
                            if(rank == office.getRank()[i]){
                                if(dollars >= office.getDollarCost()[i]){
                                    player.setDollars(player.getDollars() - office.getDollarCost()[i]);
                                    player.setRank(rank);
                                    System.out.printf("You upgraded to rank %d!%n",player.getRank());
                                }
                                else{
                                    System.out.println("You don't have enough dollars for that rank. Try again.");
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
                                    System.out.printf("you upgraded to rank %d%n",player.getRank());
                                }
                                else{
                                    System.out.println("You don't have enough credits for that rank. Try again.");
                                }
                            }
                        }
                    }
                    else{
                        System.out.println("Invalid input. Try again.");
                    }
                }
                else{
                    System.out.println("invalid rank to upgrade to");
                }
            }
            catch(Exception e){
                System.out.println("Must provide an integer rank");
            }
        }
        else{
            System.out.println("You need to be in the casting office to upgrade.");
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
                System.out.printf("%s receives %d dollars and now has %d dollars.\n",
                        player.getName(), player.getRole().getRank(), player.getDollars());
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
                        System.out.printf("%s receives %d dollars and now has %d dollars.\n",
                                player.getName(), highestDice, player.getDollars() + highestDice);
                        player.setDollars(player.getDollars() + highestDice);
                    }
                }
            }
        }
    }
}
