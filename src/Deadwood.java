import java.util.*;

public class Deadwood {

    static Stack<Card> deck = new Stack<>();
    static List<Player> players = new ArrayList<>();
    static List<Room> rooms = new ArrayList<>();
    static int days = 4;
    static int cardsOnBoard = 10;
    static int playerCount = 0;
    public static void main(String[] args){


        addPlayers();

        while(days > 0){


            while(cardsOnBoard > 1){

                for (int i = 0; i < playerCount; i++) {
                    if (cardsOnBoard == 1) {            //checks if day is over during player rotation
                        break;
                    }
                    takeTurn(players.get(i));

                }
            }


            days--;
        }
        displayTotalScores();

    }


    // Adds players to the game
    // Updates credits, rank, or number of days based on
    // number of players.
    public static void addPlayers(){
        //create scanner
        Scanner scan = new Scanner(System.in);
        System.out.println("Pick number of players (2-8): ");
        playerCount = scan.nextInt();

        //check correct number of players
        assert (playerCount >= 2 && playerCount <= 8): "Invalid number of players.";

        //initialize player and starting conditions
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Enter player %d's name: \n", i+1);
            String name = scan.next();
            players.add(new Player(name));

            if (playerCount == 5) {
                players.get(i).setCredits(2);
            } else if (playerCount == 6) {
                players.get(i).setCredits(4);
            } else if (playerCount >= 7) {
                players.get(i).setRank(2);
            }
        }

        if (playerCount <= 3) {
            days = 3;
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

    public static void displayTotalScores() {

    }



    public static void takeTurn(Player player) {
        System.out.printf("%s it's your turn to play. Choosing an invalid action will result in your turn ending.%n", player.getName());

        Scanner scan = new Scanner(System.in);

        //case for acting in role
        if(player.hasRole()){
            System.out.println("You're currently acting in a role. Options are: 'act', 'rehearse', or type anything else to end turn.");

            String choice = scan.nextLine();
            if(choice.equals("act")){
                act();
            }
            if(choice.equals("rehearse")){
                player.setRehearsalTokens(player.getRehearsalTokens() + 1);
            }

        }
        //case for moving, taking role, upgrading
        else{
            String room = player.getRoom().getName();

            //in trailer only option is move
            if(room.equals("trailer")){
                displayCurrentScores(player);
                displayRooms(player.getRoom());
                System.out.println("Would you like to move? Type 'yes' or anything else to end.");
                String choice = scan.nextLine();
                if(choice.equals("yes")){
                    move();
                }
            }
            //in casting office option to upgrade and then move
            else if(room.equals("casting office")){
                displayCurrentScores(player);
                displayRanks();
                System.out.println("Since you're in the casting office would you like to upgrade? Type 'yes' or anything else to continue.");
                String choice = scan.nextLine();
                if(choice.equals("yes")){
                    upgrade();
                }
                displayRooms(player.getRoom());
                System.out.println("Would you like to move out of the casting office? Type 'yes' to move or anything else to end.");
                choice = scan.nextLine();
                if(choice.equals("yes")){
                    move();
                }
            }
            //in any set option is takerole or move
            else{
                displayCurrentScores(player);
                displayRooms(player.getRoom());
                System.out.println("Options are: 'take role', 'move', or anything else to end.");
                String choice = scan.nextLine();
                if(choice.equals("take role")){
                    takeRole();
                }
                if(choice.equals("move")){
                    move();
                }
            }

            //if player wants to upgrade at end of turn
            if(room.equals("casting office")){
                displayCurrentScores(player);
                displayRanks();
                System.out.println("Since you're in the casting office would you like to upgrade? Type 'yes' or anything else to end.");
                String choice = scan.nextLine();
                if(choice.equals("yes")){
                    upgrade();
                }
            }
        }
        System.out.println("Turn ended.");

    }


}
