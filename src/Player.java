public class Player {
    private String name;
    private int rank;
    private int credits;
    private int dollars;
    private int rehearsalTokens;
    private String room;
    private Role role;

    //constructor
    public Player(String name) {
        this.name = name;
        this.rank = 1;
        this.rehearsalTokens = 0;
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
    
}
