import java.net.StandardSocketOptions;
import java.util.*;
import org.w3c.dom.Document;

public class Deadwood {



    public static void main(String[] args){
        Stack<Card> deck = new Stack<>();
        List<Player> players = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        int days = 4;
        int cardsOnBoard = 10;



        //populate rooms list
        Document doc1 = null;
        Document doc2 = null;
        ParseXML parsing = new ParseXML();
        try{
            doc1 = parsing.getDocFromFile("board.xml");
            parsing.readRoomData(doc1, rooms);

            doc2 = parsing.getDocFromFile("cards.xml");
            parsing.readCardData(doc2, deck);


        }
        catch(Exception e){
            System.out.println("Error = "+e);
        }

        addPlayers(args[0], players, days);
        
        while(days > 0){
            while(cardsOnBoard > 1){
                for (int i = 0; i < players.size(); i++) {
                    takeTurn(players.get(i), players);
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

    public static void createCards(){
        //parse cards from xml (including onCard roles)
        //add cards to deck
    }
    public static void createRooms(){
        //parse room details from xml (including offCard roles)
        //add rooms to list of rooms
    }

    public static void displayRooms(Room current){
        //display rooms with cards, roles and which ones are adjacent to current room
    }

    public static void displayRoles(Room current){
        //display roles in given room
    }

    public static void displayRanks(){
        //display ranks that are possible to upgrade to
        //with associated currency
    }

    public static void displayCurrentScores(Player player) {
        //display all stats about player for text based rendition
    }

    public static void displayTotalScores(){

    }

    public static void act() {
    }

    public static boolean upgrade(Player player) {
        return true;
    }

    public static boolean move(Player player) {
        return true;
    }

    public static boolean takeRole(){
        return true;
    }







    public static void takeTurn(Player player, List<Player> players) {
        System.out.printf("%s it's your turn to play.%n", player.getName());

        Scanner scan = new Scanner(System.in);
        
        boolean turn = true; // takeRole(), act(), rehearse() could return booleans to update this
        String input;
        while(turn){ //need to flip turn boolean where appropriate
            input = scan.nextLine();
            if(input.equals("end")){
                turn = false;
            }
            else if(input.equals("active player")){
                displayCurrentScores(player);
            }
            else if(input.equals("all players")){
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i) == player){
                        System.out.println("Active player: ");
                    }
                    displayCurrentScores(players.get(i));
                }
            }
            else if(input.equals("move")){
                if(player.hasRole()){
                    System.out.println("You can't move right now. Options are act, rehearse, or end.");
                }
                else{
                    move(player); //need to prompt user where to move and check that it's adjacent
                }
            }
            else if(input.equals("take role")){
                if(player.hasRole()){
                    System.out.println("You're already working. Options are act, rehearse, or end.");
                }
                else{
                    takeRole(); //need to check if there are compatible roles in room
                }
            }
            else if(input.equals("act")){
                if(!player.hasRole()){
                    System.out.println("You're not working on a role yet.");
                }
                else{
                    act(); //need to check on or off card, etc.
                }
            }
            else if(input.equals("rehearse")){
                if(player.getRehearsalTokens() < 5 && player.hasRole()){
                    player.setRehearsalTokens(player.getRehearsalTokens() + 1);
                }
                else{
                    System.out.println("You already have the max rehearsal tokens");
                }

            }
            else if(input.equals("upgrade")){
                if(player.getRoom().getName().equals("casting office")){
                    upgrade(player); //need to prompt user for rank and currency
                }
                else{
                    System.out.println("You need to be in the casting office to upgrade.");
                }
            }
            else{
                System.out.println("Unrecognized input. The options are: 'active player', 'all players', 'move', 'take role', 'rehearse', 'act', 'upgrade', and 'end'");
            }                        
        }               
        System.out.println("Turn ended.");
    }


}
