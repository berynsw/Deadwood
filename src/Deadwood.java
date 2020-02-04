import java.util.*;

public class Deadwood {

    static Stack<Card> deck = new Stack<>();
    static List<Player> players = new ArrayList<>();
    static List<Room> rooms = new ArrayList<>();
    static int DAYS = 4;
    public static void main(String[] args){


        addPlayers();
        //Create rooms by parsing XML
        //create cards by parsing XML

        //new ArrayList<>(Arrays.asList(new Role("prisoner in cell", 2, false), new Role("prisoner in cell", 2, false)))
        //list.add(new Role("prisoner in cell", 2, false));

        //Set
    }

    public static void addPlayers(){
        //create scanner
        Scanner scan = new Scanner(System.in);
        System.out.println("Pick number of players (2-8): ");
        int playerCount = scan.nextInt();

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
            } else if (playerCount >=7 ) {
                players.get(i).setRank(2);
            }
        }
        if (playerCount <= 3) {
            DAYS = 3;
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


}
