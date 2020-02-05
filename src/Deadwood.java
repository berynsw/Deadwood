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
            System.out.println("You're currently acting in a role. Your options are: 'act', 'rehearse'. Type anything else to end turn.");
            String choice = scan.nextLine();

            if(choice.equals("act")){
                act();
            }
            else if(choice.equals("rehearse")){
                player.setRehearsalTokens(player.getRehearsalTokens() + 1);
            }
            else{
                System.out.println("Your turn is over.");
            }
        }
        //case for moving, taking role, upgrading
        else{

            String room = player.getRoom().getName();

//            if(room.equals("casting office")){
//                System.out.println("Since you're in the casting office would you like to upgrade? Type 'yes' or anything else to continue to the move option.");
//            }

            if(room.equals("trailer")){
                System.out.println("You're in the trailer. Your only option is to 'move'. Type anything else to end.");
                String choice = scan.nextLine();
                if(choice.equals("move")){
                    move();
                }
            }
            else if(room.equals("casting office")){
                System.out.println("Since you're in the casting office would you like to upgrade? Type 'yes' or anything else to end.");
            }


            if(room.equals("casting office")){
                System.out.println("Since you're in the casting office would you like to upgrade? Type 'yes' or anything else to end.");
            }

        }

    }


}
