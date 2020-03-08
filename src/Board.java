/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/
import java.util.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class Board extends JFrame{

    // JLabels
    static JLabel boardlabel;
    static JLayeredPane bPane;


    static JLabel mLabel;
    JButton pIcon;
    JButton pRank;
    JButton pCredits;
    JButton pDollars;
    JButton pRehearsalTokens;
    JButton pRoom;
    JButton pRole;
    JButton pOnCard;


    //JButtons
    JButton bMove;
    JButton bTakeRole;
    JButton bRehearse;
    JButton bAct;
    JButton bUpgrade;
    JButton bEnd;


    ImageIcon icon;

    ArrayList<String> playerDice = new ArrayList<>(Arrays.asList("b1.png", "c1.png", "g1.png", "o1.png", "p1.png", "r1.png", "v1.png", "w1.png", "y1.png"));


    // Constructor
    public ArrayList<String> getPlayerDice() {
        return playerDice;
    }

    public Board() {
        // Set the title of the JFrame
        super("Deadwood");
        // Set the exit option for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the JLayeredPane to hold the display, cards, dice and buttons
        bPane = getLayeredPane();

        // Create the deadwood board
        boardlabel = new JLabel();
        icon =  new ImageIcon("images/board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));

        // Set the size of the GUI
        setSize(icon.getIconWidth()+200,icon.getIconHeight());

        mLabel = new JLabel("MENU");
        mLabel.setBounds(icon.getIconWidth()+50,0,100,20);
        bPane.add(mLabel,new Integer(2));
    }


    public void removeTurnButtons(){
        bPane.remove(mLabel);
        bPane.remove(bMove);
        bPane.remove(bTakeRole);
        bPane.remove(bRehearse);
        bPane.remove(bAct);
        bPane.remove(bUpgrade);
        bPane.remove(bEnd);
        bPane.revalidate();
        bPane.repaint();
    }
    public void createTurnButtons(){
        if(pIcon != null){
            clearPlayerStats();
        }
        showPlayerStats(Deadwood.getInstance().getCurrentPlayer());

        bMove = new JButton("MOVE");
        bMove.setBackground(Color.white);
        bMove.setBounds(icon.getIconWidth()+10,30,150, 20);
        bMove.addMouseListener(new turnMouseListener());
        bPane.add(bMove, new Integer(2));

        bTakeRole = new JButton("TAKE ROLE");
        bTakeRole.setBackground(Color.white);
        bTakeRole.setBounds(icon.getIconWidth()+10,60,150, 20);
        bTakeRole.addMouseListener(new turnMouseListener());
        bPane.add(bTakeRole, new Integer(2));

        bRehearse = new JButton("REHEARSE");
        bRehearse.setBackground(Color.white);
        bRehearse.setBounds(icon.getIconWidth()+10,90,150, 20);
        bRehearse.addMouseListener(new turnMouseListener());
        bPane.add(bRehearse, new Integer(2));

        bAct = new JButton("ACT");
        bAct.setBackground(Color.white);
        bAct.setBounds(icon.getIconWidth()+10, 120,150, 20);
        bAct.addMouseListener(new turnMouseListener());
        bPane.add(bAct, new Integer(2));

        bUpgrade = new JButton("UPGRADE");
        bUpgrade.setBackground(Color.white);
        bUpgrade.setBounds(icon.getIconWidth()+10, 150,150, 20);
        bUpgrade.addMouseListener(new turnMouseListener());
        bPane.add(bUpgrade, new Integer(2));

        bEnd = new JButton("END");
        bEnd.setBackground(Color.white);
        bEnd.setBounds(icon.getIconWidth()+10, 180,150, 20);
        bEnd.addMouseListener(new turnMouseListener());
        bPane.add(bEnd, new Integer(2));

    }
    class turnMouseListener implements MouseListener{
        public turnMouseListener(){
        }
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bMove){
                System.out.println("Move is Selected\n");
                createMoveButtons();
            }
            else if (e.getSource() == bTakeRole){
                System.out.println("takeRole is Selected\n");
                createRoleButtons();
            }
            else if (e.getSource() == bRehearse){
                Player player = Deadwood.getInstance().getCurrentPlayer();
                if(player.hasMoved() == false){
                    //act
                    //endTurn
                }
                else{
                    popUpMessage("You can't rehearse right now");
                }
            }
            else if (e.getSource() == bAct){
                Player player = Deadwood.getInstance().getCurrentPlayer();
                if(player.hasMoved() == false){
                    //act
                    //endTurn
                }
                else{
                    popUpMessage("You can't act right now");
                }
                System.out.println("Acting is Selected\n");
            }
            else if (e.getSource() == bUpgrade){

                System.out.println("Upgrade is Selected\n");
            }
            else if (e.getSource() == bEnd){
                Deadwood.getInstance().endTurn();
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
    }



    class roleMouseListener implements MouseListener{
        Set set;
        Player player;
        public roleMouseListener(Set set, Player player){
            this.set = set;
            this.player = player;
        }
        public void mouseClicked(MouseEvent e) {
            for(Role role : this.set.getRoles()){
                if(e.getSource() == role.getButton()){
                    System.out.println("clicked "+role.getName()+" offcard");

                    removeRoleButtons(set, player);

                    //player takes role
                    placePlayer(role.getX(), role.getY(), player);
                    player.setRole(role);
                    player.setOnCard(false);
                    role.setFilled(true);
                    role.setLabel(player.getLabel());

                    Deadwood.getInstance().endTurn();
                    createTurnButtons();
                }
            }
            for(Role role : this.set.getCard().getRoles()){
                if(e.getSource() == role.getButton()){
                    System.out.println("clicked "+role.getName()+" oncard");

                    removeRoleButtons(set, player);

                    //player takes role
                    placePlayer(role.getX()+set.getX(), role.getY()+set.getY(), player);
                    player.setRole(role);
                    player.setOnCard(true);
                    role.setFilled(true);
                    role.setLabel(player.getLabel());

                    Deadwood.getInstance().endTurn();
                    createTurnButtons();
                }
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
    }
    public void removeRoleButtons(Set set, Player player){
        for(Role role : set.getRoles()){
            if((!role.isFilled()) && player.getRank() >= role.getRank()){
                bPane.remove(role.getButton());
            }
        }
        for(Role role : set.getCard().getRoles()){
            if((!role.isFilled()) && player.getRank() >= role.getRank()){
                bPane.remove(role.getButton());
            }
        }
        bPane.revalidate();
        bPane.repaint();
    }
    public void createRoleButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        if(player.getRole() == null && Deadwood.getInstance().getSets().containsKey(player.getRoom())){
            removeTurnButtons();
            int y = 30;
            boolean found = false;
            Set set = Deadwood.getInstance().getSets().get(player.getRoom());
            for(Role role : set.getRoles()){
                if((!role.isFilled()) && player.getRank() >= role.getRank()){
                    found = true;
                    JButton b = new JButton("OFF: "+role.getName().toUpperCase());
                    b.setBackground(Color.white);
                    b.setBounds(icon.getIconWidth()+10,y,150, 20);
                    b.addMouseListener(new roleMouseListener(set, player));
                    bPane.add(b, 2);
                    role.setButton(b);
                    y += 30;
                }
            }
            for(Role role : set.getCard().getRoles()){
                if((!role.isFilled()) && player.getRank() >= role.getRank()){
                    found = true;
                    JButton b = new JButton("ON: "+role.getName().toUpperCase());
                    b.setBackground(Color.white);
                    b.setBounds(icon.getIconWidth()+10,y,150, 20);
                    b.addMouseListener(new roleMouseListener(set, player));
                    bPane.add(b, 2);
                    role.setButton(b);
                    y += 30;
                }
            }
            if(found == false){
                popUpMessage("You can't act here!");
                createTurnButtons();
            }
        }
        else{
            popUpMessage("You can't take a role right now!");
        }
    }



    class moveMouseListener implements MouseListener{
        Player player;
        List<String> nebs;
        public moveMouseListener(Player player, List<String> nebs){
            this.player = player;
            this.nebs = nebs;
        }
        public void mouseClicked(MouseEvent e) {
            for(String s : nebs){
                if(e.getSource() == convertToRoom(s).getButton()) {
                    System.out.println("requested move to: "+s);

                    removeMoveButtons();
                    placePlayerInRoom(player, s);
                    player.setMoved(true);

                    createTurnButtons();
                }
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
    }
    public void removeMoveButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        List<String> nebs = convertToRoom(player.getRoom()).getAdjacents();
        for(String s : nebs) {
            Room room = convertToRoom(s);
            bPane.remove(room.getButton());
        }
        bPane.revalidate();
        bPane.repaint();
    }
    public void createMoveButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        if(player.getRole() == null && player.hasMoved() == false){
            removeTurnButtons();

            List<String> nebs = convertToRoom(player.getRoom()).getAdjacents();
            int y = 30;
            for(String s : nebs){
                Room room = convertToRoom(s);
                JButton b = new JButton(room.getName().toUpperCase());
                b.setBackground(Color.white);
                b.setBounds(icon.getIconWidth()+10,y,150, 20);
                b.addMouseListener(new moveMouseListener(player, nebs));
                bPane.add(b, 2);
                room.setButton(b);
                y += 30;
            }
        }
        else{
            popUpMessage("You can't move right now!");
        }


    }
    public Room convertToRoom(String s){
        Deadwood deadwood = Deadwood.getInstance();
        Room room = null;
        if(deadwood.getSets().containsKey(s)){
            room = deadwood.getSets().get(s);
            //if(room instanceof Set)
        }
        else if(s.equalsIgnoreCase("trailer")){
            room = deadwood.getTrailer();
        }
        else if(s.equalsIgnoreCase("office")){
            room = deadwood.getOffice();
        }
        return room;
    }


    public static int getPlayerNum(){
        String[] players = {"2 players", "3 players", "4 players", "5 players", "6 players", "7 players", "8 players"};
        int[] nums = {2,3,4,5,6,7,8};
        int index =  JOptionPane.showOptionDialog(null, "How many players?", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, players, players[0]);
        return nums[index];
    }

    public static void placeCard(Set set, String file){
        // Add a scene card to this room
        JLabel cardlabel = new JLabel();
        ImageIcon cIcon =  new ImageIcon(file);
        cardlabel.setIcon(cIcon);
        cardlabel.setBounds(set.getX(),set.getY(),205,115);
        cardlabel.setOpaque(true);
        // Add the card to the lower layer
        bPane.add(cardlabel, new Integer(1));
        set.setCardIcon(cardlabel);
    }

    public static void removeCard(Set set){
        bPane.remove(set.getCardIcon());
        bPane.revalidate();
        bPane.repaint();
    }
    public static void flipCard(Set set){
        removeCard(set);
        placeCard(set, set.getCard().getImage());
    }


   
    public static void placeShots(Set set) {
        for (Shot shot : set.getShotList()) {
            JLabel shotlabel = new JLabel();
            ImageIcon cIcon = new ImageIcon("images/shot.png");
            shotlabel.setIcon(cIcon);
            shotlabel.setBounds(shot.getX(), shot.getY(), cIcon.getIconHeight(), cIcon.getIconWidth());
            shotlabel.setOpaque(true);
            // Add the card to the lower layer
            bPane.add(shotlabel, new Integer(1));
            shot.setIcon(shotlabel);
        }
    }


    public static void removeShot(Set set){
        for(Shot shot : set.getShotList()){
            JLabel icon = shot.getIcon();
            if(icon != null){
                bPane.remove(icon);
                bPane.revalidate();
                bPane.repaint();
                set.setCurrentShots(set.getCurrentShots()-1);
            }
        }
        if(set.getCurrentShots() == 0){
            removeCard(set);
            //Deadwood.getInstance().payOut();
        }
    }

    public static void placePlayer(int x, int y, Player player) {
        if(player.getLabel() != null){
            removePlayer(player);
        }
        JLabel playerLabel = new JLabel();
        ImageIcon pIcon = new ImageIcon(player.getIcon());
        playerLabel.setIcon(pIcon);
        playerLabel.setBounds(x, y, 46, 46);
        playerLabel.setOpaque(true);
        player.setLabel(playerLabel);
        bPane.add(playerLabel, new Integer(4));
    }
    public static void removePlayer(Player player) {
        JLabel rm = player.getLabel();
        bPane.remove(rm);
        bPane.revalidate();
        bPane.repaint();
    }

    public void placePlayerInRoom(Player player, String roomString){
        if(player.getRoom() != null){
            Room previous = convertToRoom(player.getRoom());
            previous.setPlayerCount(previous.getPlayerCount()-1);
        }


        Room room = convertToRoom(roomString);
        int x = 0;
        int y = 0;
        int playerCount = room.getPlayerCount();
        if(roomString.equalsIgnoreCase("trailer")){
            x = 991;
            y = 270;
            if(playerCount >= 4){
                y += 50;
                x += 50*(playerCount-4);
            }
            else{
                x += 50*playerCount;
            }
        }
        else if(roomString.equalsIgnoreCase("office")){
            x = 9;
            y = 469;
            if(playerCount >= 4){
                y += 50;
                x += 50*(playerCount-4);
            }
            else{
                x += 50*playerCount;
            }
        }
        else if(Deadwood.getInstance().getSets().containsKey(roomString.toLowerCase())){
            y = room.getY()+room.getH();
            x = room.getX()+35*playerCount-20;
        }

        placePlayer(x, y, player);

        player.setRoom(roomString);
        room.setPlayerCount(playerCount+1);
        if(room instanceof Set){
            Set set = (Set)room;
            if(set.isCardFlipped() == false){
                flipCard(set);
                set.setCardFlipped(true);
            }
        }
    }





    public void clearPlayerStats(){
        bPane.remove(pIcon);
        bPane.remove(pRank);
        bPane.remove(pCredits);
        bPane.remove(pDollars);
        bPane.remove(pRehearsalTokens);
        bPane.remove(pRoom);
        bPane.remove(pRole);
        bPane.remove(pOnCard);
        bPane.revalidate();
        bPane.repaint();
    }
    public void showPlayerStats(Player player){
        pIcon = new JButton("Icon: "+player.getIcon().substring(12));
        pIcon.setBackground(Color.white);
        pIcon.setBounds(icon.getIconWidth()+10, 240,150, 20);
        bPane.add(pIcon, new Integer(2));

        //JButton pRank;
        pRank = new JButton("Rank: "+player.getRank());
        pRank.setBackground(Color.white);
        pRank.setBounds(icon.getIconWidth()+10, 270,150, 20);
        bPane.add(pRank, new Integer(2));

        //JButton pCredits;
        pCredits = new JButton("Credits: "+player.getCredits());
        pCredits.setBackground(Color.white);
        pCredits.setBounds(icon.getIconWidth()+10, 300,150, 20);
        bPane.add(pCredits, new Integer(2));

        //JButton pDollars;
        pDollars = new JButton("Dollars: "+player.getDollars());
        pDollars.setBackground(Color.white);
        pDollars.setBounds(icon.getIconWidth()+10, 330,150, 20);
        bPane.add(pDollars, new Integer(2));

        //JButton pRehearsalTokens;
        pRehearsalTokens = new JButton("Rehears Toks: "+player.getRehearsalTokens());
        pRehearsalTokens.setBackground(Color.white);
        pRehearsalTokens.setBounds(icon.getIconWidth()+10, 360,150, 20);
        bPane.add(pRehearsalTokens, new Integer(2));

        //JButton pRoom;
        pRoom = new JButton("Room: "+player.getRoom());
        pRoom.setBackground(Color.white);
        pRoom.setBounds(icon.getIconWidth()+10, 390,150, 20);
        bPane.add(pRoom, new Integer(2));

        //JButton pRole;
        pRole = new JButton("Role: "+player.getRole());
        pRole.setBackground(Color.white);
        pRole.setBounds(icon.getIconWidth()+10, 420,150, 20);
        bPane.add(pRole, new Integer(2));

        //JButton pOnCard;
        pOnCard = new JButton("OnCard?: "+player.isOnCard());
        pOnCard.setBackground(Color.white);
        pOnCard.setBounds(icon.getIconWidth()+10, 450,150, 20);
        bPane.add(pOnCard, new Integer(2));
    }

    public static void endOfDayDialog(){
        int day = Deadwood.getInstance().getDays()+1;
        JOptionPane.showMessageDialog(null, "Day "+day+" ended.");
    }
    public static void popUpMessage(String s){
        JOptionPane.showMessageDialog(null, s);
    }
}
