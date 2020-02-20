import java.sql.SQLOutput;
import java.util.*;
import org.w3c.dom.Document;

public class Deadwood {

    //Initialize earnings
    static private int[] RANK = {2,3,4,5,6};
    static private int[] DOLLAR_COST = {4,10,18,28,40};
    static private int[] CREDIT_COST = {5,10,15,20,25};
    static final String LINE = "----------------------------------------------";
    //static private List<Player> players = new ArrayList<>();

    public static void main(String[] args){
        //initialize room/card data structure
        Stack<Card> deck = new Stack<>();
        List<Player> players = new ArrayList<>();
        HashMap<String,Set> sets = new HashMap<>();
        Room trailer = null;
        Room office = null;
        int days = 4;
        int cardsOnBoard = 10;



        //populate rooms list
        Document doc1;
        Document doc2;
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

        for(Set set : sets.values()){
            System.out.println("room name: " + set.getName());
            for(String neb : set.getAdjacents()){
                System.out.println(" neb: "+neb);
            }
        }

        System.out.println("trailer name: " + trailer.getName());
        for(String name : trailer.getAdjacents()){
            System.out.println(" neb: "+name);
        }
        System.out.println("office name: " + office.getName());
        for(String name : office.getAdjacents()){
            System.out.println(" neb: "+name);
        }
        System.out.println();


        addPlayers(args[0], players, days);
        
        while(days > 0){
            initDay(players, sets, deck);
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

    //puts the players in trailer and puts a card from the deck into every set
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


                players.get(i).setRank(6);



            } else if (playerCount == 5) {
                players.get(i).setCredits(2);
            } else if (playerCount == 6) {
                players.get(i).setCredits(4);
            } else if (playerCount >= 7) {
                players.get(i).setRank(2);
            }
        }
        System.out.println();
    }



    public static void displayRooms(Room current){
        //display rooms with cards, roles and which ones are adjacent to current room
    }

    public static void displayRoles(List<Role> offCard, List<Role> onCard){
        System.out.println("Here are the roles available:");
        System.out.printf(" Off card: %n");
        for(Role role : offCard){
            if(!role.isFilled()){
                System.out.printf("  name: %s, rank required: %d%n", role.getName(), role.getRank());
            }
        }
        System.out.printf(" On card: %n");
        for(Role role : onCard){
            if(!role.isFilled()){
                System.out.printf("  name: %s, rank required: %d%n", role.getName(), role.getRank());
            }
        }
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

    private static void payOut(List<Player> p, Set set) {
        //initialize an arraylist of players that are on the set
        List<Player> playersOnSet = new ArrayList<>();
        List<Player> playersOnCard = new ArrayList<>();
        List<Player> playersOffCard = new ArrayList<>();
        List<Integer> dice = new ArrayList<>();
        Random r = new Random();

        //generate list of dice rolls equivalent to the budget of the movie
        int budget = set.getCard().getBudget();
        for (int i = 0; i < budget; i++) {
            dice.add(r.nextInt(6)+1);
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
            if (player.getRole().isOnCard()) {
                playersOnCard.add(player);

            } else {
                playersOffCard.add(player);
            }
        }

        //initialize static set of onCard players
        List<Player> onCard = playersOnCard;

        //only payout players if there is at least 1 player working onCard
        if(playersOnCard.size() > 0) {

            //offCard players receive dollars equal to the rank of their role
            for (Player player : playersOffCard) {
                int reward = player.getRole().getRank();
                player.setDollars(player.getDollars() + reward);
                System.out.printf("%s receives %d dollars and now has %d dollars.\n",
                        player.getName(), player.getRole().getRank(), player.getDollars());
            }

            Player highestRole;
            while (!dice.isEmpty()) {

                //if we payout each player, reset the arraylist of Oncard players and do it again
                if (playersOnCard.isEmpty()) {
                    playersOnCard = onCard;
                }

                //find player with highest role on card
                //Player highestRole = playersOnCard.get(0);

                highestRole = onCard.get(0);

                for (Player player : playersOnCard) {
                    if (player.getRole().getRank() >= highestRole.getRole().getRank()) {
                        highestRole = player;
                    }
                }
                
                // give highest ranking player a dollar equivalent of the highest die roll,
                // then remove highestPlayer and highest die from arrayList

                System.out.printf("%s receives %d dollars and now has %d dollars.\n",
                        highestRole.getName(), dice.get(dice.size()-1), (highestRole.getDollars() + dice.get(dice.size()-1)));
                highestRole.setDollars(highestRole.getDollars() + dice.remove(dice.size()-1));
                playersOnCard.remove(highestRole);
            }
        }
        System.out.println(LINE);
    }

    public static void act(Player player, Set set, List<Player> players) {

        Random r = new Random();
        int roll = r.nextInt(6) + 1;
        int budget = set.getCard().getBudget();

        if (set.getCurrentShots() == 1) {
            System.out.println("There is 1 shot left.");
        } else {
            System.out.println("There are " + set.getCurrentShots() + " shots left.");
        }


        System.out.printf("You need to roll a %d...\n", budget);
        System.out.printf("You rolled a %d!\n", roll);
        if (budget > roll) {
            System.out.println("Apparently, you're not as good an actor as you think.");

            //if failed off-card, still get a dollar
            if (!player.getRole().isOnCard()) {
                player.setDollars(player.getDollars() + 1);
                System.out.println("As compensation, you get 1 dollar.");
            } else {
                System.out.println("As compensation, you get nothing. Better luck next time!");
            }


            //decrement shot counter
            if (set.currentShots > 1) {
                set.setCurrentShots(set.currentShots - 1);
            } else {
                System.out.println("That about wraps it up, the scene is over!");
                set.setCurrentShots(0);
                System.out.println(LINE);
                payOut(players, set);

                //player is no longer in a role
                for(Player p : players) {
                    p.setRole(null);
                }
            }

        } else {
            System.out.println("It appears that you're not as bad an actor as you look!");

            if (player.getRole().isOnCard()) {
                player.setCredits(player.getCredits() + 2);
                System.out.println("As compensation, you get 2 credits.");
            } else {
                player.setCredits(player.getCredits() + 1);
                player.setDollars(player.getDollars() + 1);
                System.out.println("As compensations, you get 1 dollar and 1 credit.");
            }

            if (set.currentShots > 1) {
                set.setCurrentShots(set.currentShots - 1);
            } else {
                System.out.println("That about wraps it up, the scene is over!");
                set.setCurrentShots(0);
                System.out.println(LINE);
                payOut(players, set);

                //player is no longer in a role
                for(Player p : players) {
                    p.setRole(null);
                }
            }
        }
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
                    if(!role.isFilled()){
                        player.setRole(role);
                        role.setFilled(true);
                        System.out.printf("You took %s!%n", role.getName());
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
    }

    public static void work(Player player, Set set, Scanner scan){
        List<Role> offCard = set.getRoles();
        List<Role> onCard = set.getCard().getRoles();
        displayRoles(offCard, onCard);
        System.out.println("If you would like to work on one of these roles type the role 'name', otherwise your turn will end.");
        String input = scan.nextLine().toLowerCase();
        takeRole(player, offCard, input);
        takeRole(player, onCard, input);
    }

    //return the list of rooms adjacent to the current location
    // cases for if the current room is trailer office or set
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


    public static void takeTurn(Player player, List<Player> players, HashMap<String, Set> sets, Room trailer, Room office) {
        System.out.printf("%s it's your turn to play.%n", player.getName());
        Scanner scan = new Scanner(System.in);
        boolean turn = true; // takeRole(), act(), rehearse() could return booleans to update this
        String input;


        while(turn){
            System.out.printf("The options are: 'active player', 'all players', 'move', 'work', 'rehearse', 'act', 'upgrade', and 'end'.%n");
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
            else if(input.equalsIgnoreCase("act")) {
                act(player, sets.get(player.getRoom()), players);
                turn = false;
            }
            else if(input.equalsIgnoreCase("move")){ //done
                if(player.getRole() != null){
                    System.out.println("You are acting in a role and can't move. Options are act, rehearse, or end.");
                }
                else{


                    System.out.println("The neighboring rooms to your current room are:");
                    List<String> neighbors = getNeighbors(player.getRoom(), sets, trailer, office);
                    for(String room : neighbors){
                        System.out.println(" "+room);
                    }

                    System.out.println("Where would you like to move to?");
                    input = scan.nextLine().toLowerCase();

                    //check if the move request is a neighboring room
                    if(neighbors.contains(input)){
                        player.setRoom(input);
                        System.out.println("You successfully moved to "+input);
                        //if it's a set
                        if(sets.containsKey(input)){
                            if(sets.get(input).getCard() != null){
                                work(player, sets.get(input), scan);
                            }
                            else{
                                System.out.println("This set is wrapped. You can't work here today.");
                            }
                        }
                        else if(input.equals("office")){
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
                    if(player.getRole() != null){
                        System.out.println("You're already working. Options are act, rehearse, or end.");
                    }
                    else{
                        Set set = sets.get(player.getRoom());
                        //if the set is active
                        if(set.getCard() != null){
                            work(player, sets.get(player.getRoom()), scan);
                            turn = false;
                        }
                        else{
                            System.out.println("This set is wrapped. You can't work here today.");
                        }
                    }
                }
            }
            else if(input.equalsIgnoreCase("act")){
                if(player.getRole() == null){
                    System.out.println("You're not working on a role yet.");
                }
                else{
                    //act(); //need to check on or off card, etc.

                    turn = false;
                }
            }
            else if(input.equalsIgnoreCase("rehearse")){ //done
                if(player.getRole() != null){
                    int budget = sets.get(player.getRoom()).getCard().getBudget();
                    if(player.getRehearsalTokens() < budget-1) {
                        player.setRehearsalTokens(player.getRehearsalTokens() + 1);
                        System.out.println("+1 practice chips!");
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
                System.out.println("Unrecognized input.");
            }                        
        }               
        System.out.printf("%s's turn ended.%n%n",player.getName());
    }

}
