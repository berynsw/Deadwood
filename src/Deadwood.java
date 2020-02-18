import java.util.*;
import org.w3c.dom.Document;

public class Deadwood {


    static int[] RANK = {2,3,4,5,6};
    static int[] DOLLAR_COST = {4,10,18,28,40};
    static int[] CREDIT_COST = {5,10,15,20,25};

    public static void main(String[] args){
        Stack<Card> deck = new Stack<>();
        List<Player> players = new ArrayList<>();
        HashMap<String,Set> sets = new HashMap<>();
        Room trailer = null;
        Room office = null;
        int days = 4;
        int cardsOnBoard = 10;



        //populate rooms list
        Document doc1 = null;
        Document doc2 = null;
        ParseXML parsing = new ParseXML();
        try{
            //rooms
            doc1 = parsing.getDocFromFile("board.xml");
            sets = parsing.readSetData(doc1);
            trailer = parsing.readTrailerData(doc1);
            office = parsing.readOfficeData(doc1);
            //cards
            doc2 = parsing.getDocFromFile("cards.xml");
            parsing.readCardData(doc2, deck);


        }
        catch(Exception e){
            System.out.println("Error = "+e);
        }

        for(String name : sets.keySet()){
            System.out.println("room name: " + name);
        }
        System.out.println("office name: " + office.getName());
        System.out.println("trailer name: " + trailer.getName());


        addPlayers(args[0], players, days);
        
        while(days > 0){
            while(cardsOnBoard > 1){
                for (int i = 0; i < players.size(); i++) {
                    takeTurn(players.get(i), players, sets, trailer, office);
                    if (cardsOnBoard == 1) {            //checks if day is over during player rotation
                        break;
                    }                    
                }
            }
            days--;
        }
        displayTotalScores();
    }


    // Adds players to the game
    // Updates credits, rank, or number of days based on
    // number of players.
    public static void addPlayers(String arg, List<Player> players, int days) {
        //get number of players from args
        int playerCount = 0;
        try{
            playerCount = Integer.parseInt(arg);
        }
        catch(Exception e){
            System.out.println("Must provide an integer number of players");
        }


        //check correct number of players
        //check valid number of players
        if (playerCount < 2 || playerCount > 8) {
            System.out.println("Invalid number of players.");
            System.exit(0);
        }

        //create scanner
        Scanner scan = new Scanner(System.in);

        //initialize player and starting conditions
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Enter player %d's name: \n", i + 1);
            String name = scan.next();
            players.add(new Player(name));
            if (playerCount <= 3) {
                days = 3;
            } else if (playerCount == 5) {
                players.get(i).setCredits(2);
            } else if (playerCount == 6) {
                players.get(i).setCredits(4);
            } else if (playerCount >= 7) {
                players.get(i).setRank(2);
            }
        }
    }



    public static void displayRooms(Room current){
        //display rooms with cards, roles and which ones are adjacent to current room
    }

    public static void displayRoles(List<Role> offCard, List<Role> onCard){
        System.out.println("Here are the roles available:");
        System.out.printf(" Off card: %n");
        for(Role role : offCard){
            if(!role.isFilled()){
                System.out.printf("  name: %s rank required: %d%n", role.getName(), role.getRank());
            }
        }
        System.out.printf(" On card: %n");
        for(Role role : offCard){
            if(!role.isFilled()){
                System.out.printf("  name: %s rank required: %d%n", role.getName(), role.getRank());
            }
        }
    }

    public static void displayRanks(){
        //display ranks that are possible to upgrade to
        //with associated currency
    }

    public static void displayCurrentStats(Player player) {
        System.out.printf("name: %s%n", player.getName());
        System.out.printf("room: %s%n", player.getRoom());
        System.out.printf("rank: %d%n", player.getRank());
        System.out.printf("credits: %d%n", player.getCredits());
        System.out.printf("dollars: %s%n", player.getDollars());
    }

    public static void displayTotalScores(){

    }

    public static void act() {
    }

    public static void upgrade(Player player, Scanner scan) {
        System.out.println("The upgrade options are:");
        for(int i = 0; i < 5; i++){
            System.out.printf(" rank %d: %d dollars, %d credits%n", RANK[i], DOLLAR_COST[i], CREDIT_COST[i]);
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
                        if(rank == RANK[i]){
                            if(dollars >= DOLLAR_COST[i]){
                                player.setDollars(player.getDollars() - DOLLAR_COST[i]);
                                player.setRank(rank);
                                System.out.printf("you upgraded to rank %d%n",player.getRank());
                            }
                        }
                    }
                    System.out.println("You don't have enough dollars for that rank. Try again.");
                }
                else if(input.equalsIgnoreCase("credits")){
                    int credits = player.getCredits();
                    for(int i = 0; i < 5; i++){
                        if(rank == RANK[i]){
                            if(credits >= CREDIT_COST[i]){
                                player.setCredits(player.getCredits() - CREDIT_COST[i]);
                                player.setRank(rank);
                                System.out.printf("you upgraded to rank %d%n",player.getRank());
                            }
                        }
                    }
                    System.out.println("You don't have enough credits for that rank. Try again.");
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



    //searches through list of roles for specified role name (on or off card) and allows player to take that role if possible
    public static void takeRole(Player player, List<Role> roles, String input){
        for(Role role : roles){
            if(input.equalsIgnoreCase(role.getName())){
                if(player.getRank() >= role.getRank()){
                    player.setRole(role);
                    System.out.printf("You took %s!%n", role.getName());
                }
                else{
                    System.out.println("You're not high enough rank");
                }
            }
        }
    }

    public static void work(Player player, Set set, Scanner scan){

    }



    public static void takeTurn(Player player, List<Player> players, HashMap<String, Set> sets, Room trailer, Room office) {
        System.out.printf("%s it's your turn to play.%n", player.getName());

        Scanner scan = new Scanner(System.in);
        
        boolean turn = true; // takeRole(), act(), rehearse() could return booleans to update this
        String input;

        while(turn){
            input = scan.nextLine();
            if(input.equalsIgnoreCase("end")){
                turn = false;
            }
            else if(input.equalsIgnoreCase("active player")){
                displayCurrentStats(player);
            }
            else if(input.equalsIgnoreCase("all players")){
                for(Player p : players){
                    if(p == player){
                        System.out.println("Active player: ");
                    }
                    displayCurrentStats(p);
                    System.out.println();
                }
            }
            else if(input.substring(0,4).equalsIgnoreCase("move")){ //done
                if(player.hasRole()){
                    System.out.println("You can't move right now. Options are act, rehearse, or end.");
                }
                else{
                    String move = input.substring(4).toLowerCase();
                    List<String> neighbors = sets.get(player.getRoom()).getAdjacents();
                    //check if the move request is a neighboring room
                    if(neighbors.contains(move)){
                        player.setRoom(move);
                        //if it's a set
                        if(sets.containsKey(move)){
                            Set set = sets.get(move);
                            work(player, set, scan);
                        }
                        else if(move.equals("office")){
                            System.out.println("Since you moved to the office would you like to upgrade?");
                            upgrade(player, scan);
                        }
                        turn = false;
                    }
                    else{
                        System.out.println("Room is either invalid or not next to the room you're in. Try again.");
                    }
                }
            }
            else if(input.equalsIgnoreCase("work")){ //done
                if(!sets.containsKey(player.getRoom())){
                    System.out.println("You're not in a set. You must move to a set to work.");
                }
                else{
                    if(player.hasRole()){
                        System.out.println("You're already working. Options are act, rehearse, or end.");
                    }
                    else{
                        Set set = sets.get(player.getRoom());
                        //if the set is active
                        if(set.getCard() != null){
                            List<Role> offCard = set.getRoles();
                            List<Role> onCard = set.getCard().getRoles();
                            displayRoles(offCard, onCard);
                            System.out.println("If you would like to work on one of these roles type the role 'name', otherwise your turn will end.");
                            input = scan.nextLine().toLowerCase();
                            takeRole(player, offCard, input);
                            takeRole(player, onCard, input);
                            turn = false;
                        }
                        else{
                            System.out.println("This set is wrapped. You can't work here today.");
                        }
                    }
                }
            }
            else if(input.equalsIgnoreCase("act")){
                if(!player.hasRole()){
                    System.out.println("You're not working on a role yet.");
                }
                else{
                    //act(); //need to check on or off card, etc.
                    turn = false;
                }
            }
            else if(input.equalsIgnoreCase("rehearse")){ //done
                if(player.hasRole()){
                    int budget = sets.get(player.getRoom()).getCard().getBudget();
                    if(player.getRehearsalTokens() < budget-1) {
                        player.setRehearsalTokens(player.getRehearsalTokens() + 1);
                        turn = false;
                    }
                    else{
                        System.out.println("You already have max rehearsal tokens for this cards budget. Try acting!");
                    }
                }
                else{
                    System.out.println("You aren't working.");
                }
            }
            else if(input.equalsIgnoreCase("upgrade")){
                if(player.getRoom().equalsIgnoreCase("office")){
                    upgrade(player, scan);
                }
                else{
                    System.out.println("You need to be in the casting office to upgrade.");
                }
            }
            else{
                System.out.println("Unrecognized input. The options are: 'active player', 'all players', 'move roomName', 'work', 'rehearse', 'act', 'upgrade', and 'end'");
            }                        
        }               
        System.out.println("Turn ended.");
    }



}
