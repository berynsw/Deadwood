import java.util.*;
import org.w3c.dom.Document;

public class Deadwood {


    private static String LINE = "--------------------------------------------------------";
    private static int cardsOnBoard = 10;
    private static int days = 4;

    public static void main(String[] args){

        //initialize board structures
        List<Player> players = new ArrayList<>();
        HashMap<String,Set> sets = new HashMap<>();
        Stack<Card> deck = new Stack<>();
        Room trailer = null;
        Office office = null;

        //populate rooms and cards from xml
        Document doc1;
        Document doc2;
        ParseXML parsing = new ParseXML();
        try{
            //parse rooms
            doc1 = parsing.getDocFromFile("board.xml");
            sets = parsing.readSetData(doc1);
            trailer = parsing.readTrailerData(doc1);
            office = parsing.readOfficeData(doc1);

            //parse cards
            doc2 = parsing.getDocFromFile("cards.xml");
            parsing.readCardData(doc2, deck);
        }
        catch(Exception e){
            System.out.println("Error = "+e);
        }


        addPlayers(args, players);

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
        displayTotalScores(players);
    }


    // Adds players to the game
    //  Updates credits, rank, or number of days based on number of players.
    public static void addPlayers(String[] arg, List<Player> players) {
        //get number of players from args
        int playerCount = 0;
        try{
            playerCount = Integer.parseInt(arg[0]);
        }
        catch(Exception e){
            System.out.println("Must provide one arg, an integer number of players.");
        }

        //check valid number of players
        if (playerCount < 2 || playerCount > 8) {
            System.out.println("Number of players must be between 2-8.");
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
        cardsOnBoard = 10;
    }

    // Script for an individual player's turn
    public static void takeTurn(Player player, List<Player> players, HashMap<String, Set> sets, Room trailer, Office office) {
        System.out.printf("%s it's your turn to play.%n", player.getName());
        Scanner scan = new Scanner(System.in);
        boolean turn = true; // takeRole(), act(), rehearse() could return booleans to update this
        String input;

        while(turn){
            System.out.printf("The options are: 'active player', 'all players', 'move', 'work', 'rehearse', 'act', 'upgrade', and 'end'.%n");
            input = scan.nextLine().toLowerCase();
            
            switch(input){
				case "end":
					turn = false;
					break;
				case "active player":
					displayCurrentStats(player);
					break;
				case "all players":
					for(Player p : players){
						if(p == player){
							System.out.println("Active player: ");
						}
						displayCurrentStats(p);
						System.out.println();
					}
					break;
				case "act":
					if (player.getRole()!= null) {
						act(player, sets.get(player.getRoom()), players);
						turn = false;
					} else {
						System.out.println("You are not currently acting in a role.");
					}
					break;
				case "move":
					turn = move(player, sets, trailer, office, scan);
					break;
				case "work":
					if(!sets.containsKey(player.getRoom())){
                    	System.out.println("You're not in a set. You must move to a set to work.");
					}
					else{
						turn = work(player, sets.get(player.getRoom()), scan);
					}
					break;
				case "rehearse":
					turn = rehearse(player, sets);
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

    // Displays available roles (on and off card) in current room
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

    // Display a players stats
    public static void displayCurrentStats(Player player) {
        System.out.printf("name: %s%n", player.getName());
        System.out.printf("room: %s%n", player.getRoom());
        System.out.printf("rank: %d%n", player.getRank());
        System.out.printf("credits: %d%n", player.getCredits());
        System.out.printf("dollars: %s%n", player.getDollars());
    }

    // Display final scores
    public static void displayTotalScores(List<Player> players){
        for(Player player : players) {
            System.out.printf("%s's total score: %d\n!",player.getName(), totalScore(player));
        }
        System.out.println(LINE);
    }

    // Calculate final scores
    public static int totalScore (Player player) {
        int dollars = player.getDollars();
        int credits = player.getCredits();
        int rank = player.getRank()*5;
        return dollars + credits + rank;
    }

    // Display neighboring room, calls work() and upgrade() if appropriate
    public static boolean move(Player player, HashMap<String, Set> sets, Room trailer, Office office, Scanner scan){
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
                    upgrade(office, player, scan);
                }
                return false;
            }
            else{
                System.out.println("Room is either invalid or not next to the room you're in. Try again.");
            }
        }
        return true;
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

    // Wrapper function for obtaining a new role
    public static boolean work(Player player, Set set, Scanner scan){
        if(player.getRole() != null){
            System.out.println("You're already working. Options are act, rehearse, or end.");
        }
        else{
            //if the set is active
            if(set.getCard() != null){
                List<Role> offCard = set.getRoles();
                List<Role> onCard = set.getCard().getRoles();
                displayRoles(offCard, onCard);
                System.out.println("If you would like to work on one of these roles type the role 'name', otherwise your turn will end.");
                String input = scan.nextLine().toLowerCase();
                takeRole(player, offCard, input);
                takeRole(player, onCard, input);
                return false;
            }
            else{
                System.out.println("This set is wrapped. You can't work here today.");
            }
        }
        return true;
    }

    // Searches through list of roles for specified role name (on or off card) and allows player to take that role if possible
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
    private static void payOut(List<Player> p, Set set) {
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

    public static void act(Player player, Set set, List<Player> players) {
        System.out.println(LINE);
        Random r = new Random();
        int roll = r.nextInt(6) + 1;
        int budget = set.getCard().getBudget();

        System.out.printf("You need to roll a %d...\n", budget);
        System.out.printf("You rolled a %d and you have %d rehearsal chips!\n", roll, player.getRehearsalTokens());

        //if you fail acting
        if (budget > roll + player.getRehearsalTokens()) {
            System.out.println("Apparently, you're not as good an actor as you think.");

            //if failed off-card, still get a dollar
            if (!player.getRole().isOnCard()) {
                player.setDollars(player.getDollars() + 1);
                System.out.println("As compensation, you get 1 dollar.");
            } else {
                System.out.println("As compensation, you get nothing. Better luck next time!");
            }
        }
        //if you succeed acting
        else {
            System.out.println("It appears you're not as bad an actor as you look.");

            if (player.getRole().isOnCard()) {
                player.setCredits(player.getCredits() + 2);
                System.out.println("As compensation, you get 2 credits.");
            } else {
                player.setCredits(player.getCredits() + 1);
                player.setDollars(player.getDollars() + 1);
                System.out.println("As compensations, you get 1 dollar and 1 credit.");
            }
            //decrement shot counter
            if (set.currentShots > 1) {
                set.setCurrentShots(set.currentShots - 1);
                if (set.getCurrentShots() > 1) {
                    System.out.println("You remove one shot counter. There are " + set.getCurrentShots() + " shots left.");
                } else {
                    System.out.println("You remove one shot counter. There is 1 shot left.");
                }
            } else {
                System.out.println(LINE);
                System.out.println("That about wraps it up, the scene is over!");
                set.setCurrentShots(0);
                payOut(players, set);
                //player is no longer in a role
                List<Player> playersOnSet = new ArrayList<>();
                for (int j = 0; j < players.size(); j++) {
                    if (set.getName().equals(players.get(j).getRoom())) {
                        playersOnSet.add(players.get(j));
                    }
                }
                for (Player endPlayerRole : playersOnSet) {
                    endPlayerRole.setRole(null);
                    endPlayerRole.setRehearsalTokens(0);
                }
                set.setCard(null);
                cardsOnBoard--;
            }
        }
        System.out.println(LINE);
    }
}
