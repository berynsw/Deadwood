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


    static JLabel playerlabel;
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


        // Add a dice to represent a player.
        // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon("r2.png");
        playerlabel.setIcon(pIcon);
        //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
        playerlabel.setBounds(114,227,46,46);
        playerlabel.setVisible(false);
        bPane.add(playerlabel,new Integer(3));
    }


    public void removeTurnButtons(){
        bPane.remove(mLabel);
        bPane.remove(bMove);
        bPane.remove(bTakeRole);
        bPane.remove(bRehearse);
        bPane.remove(bAct);
        bPane.remove(bUpgrade);
        bPane.revalidate();
        bPane.repaint();
    }
    public void createTurnButtons(Player player){
        bMove = new JButton("MOVE");
        bMove.setBackground(Color.white);
        bMove.setBounds(icon.getIconWidth()+10,30,150, 20);
        bMove.addMouseListener(new turnMouseListener(player));
        bPane.add(bMove, new Integer(2));

        bTakeRole = new JButton("TAKE ROLE");
        bTakeRole.setBackground(Color.white);
        bTakeRole.setBounds(icon.getIconWidth()+10,60,150, 20);
        bTakeRole.addMouseListener(new turnMouseListener(player));
        bPane.add(bTakeRole, new Integer(2));

        bRehearse = new JButton("REHEARSE");
        bRehearse.setBackground(Color.white);
        bRehearse.setBounds(icon.getIconWidth()+10,90,150, 20);
        bRehearse.addMouseListener(new turnMouseListener(player));
        bPane.add(bRehearse, new Integer(2));

        bAct = new JButton("ACT");
        bAct.setBackground(Color.white);
        bAct.setBounds(icon.getIconWidth()+10, 120,150, 20);
        bAct.addMouseListener(new turnMouseListener(player));
        bPane.add(bAct, new Integer(2));

        bUpgrade = new JButton("ACT");
        bUpgrade.setBackground(Color.white);
        bUpgrade.setBounds(icon.getIconWidth()+10, 120,150, 20);
        bUpgrade.addMouseListener(new turnMouseListener(player));
        bPane.add(bUpgrade, new Integer(2));

    }
    class turnMouseListener implements MouseListener{

        public turnMouseListener(Player player){
        }
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bMove){
                System.out.println("Move is Selected\n");
                removeTurnButtons();
                createMoveButtons();
            }
            else if (e.getSource() == bTakeRole){
                System.out.println("takeRole is Selected\n");
//                Deadwood deadwood = Deadwood.getInstance();
//                if(player.getRole() == null && deadwood.getSets().containsKey(player.getRoom())){
                removeTurnButtons();
                Player player = Deadwood.getInstance().getCurrentPlayer();
                createRoleButtons(Deadwood.getInstance().getSets().get(player.getRoom()), player);
                Deadwood.getInstance().notify();
//                }
            }
            else if (e.getSource() == bRehearse){
                System.out.println("Rehearse is Selected\n");
            }
            else if (e.getSource() == bAct){
                playerlabel.setVisible(true);
                System.out.println("Acting is Selected\n");
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
                    //takeRole(role)
                }
            }
            for(Role role : this.set.getCard().getRoles()){
                if(e.getSource() == role.getButton()){
                    System.out.println("clicked "+role.getName()+" oncard");
                    //takeRole(role)
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
    public void removeRoleButtons(){

    }
    public void createRoleButtons(Set set, Player player){
        for(Role role : set.getRoles()){
            if((!role.isFilled()) && player.getRank() >= role.getRank()){
                JButton b = new JButton();
                b.setBounds(role.getX(), role.getY(), 43, 43);
                b.addMouseListener(new roleMouseListener(set, player));
                b.setContentAreaFilled(false);
                b.setBorder(BorderFactory.createBevelBorder(1));
                bPane.add(b, 2);
                role.setButton(b);
            }
        }
        for(Role role : set.getCard().getRoles()){
            if((!role.isFilled()) && player.getRank() >= role.getRank()){
                JButton b = new JButton();
                b.setBounds(set.getX()+role.getX(), set.getY()+role.getY(), 43, 43);
                b.addMouseListener(new roleMouseListener(set, player));
                b.setContentAreaFilled(false);
                b.setBorder(BorderFactory.createBevelBorder(1));
                bPane.add(b, 2);
                role.setButton(b);
            }
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
        }
    }
    public void createMoveButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        List<String> nebs = convertToRoom(player.getRoom()).getAdjacents();
        for(String s : nebs){
            Room room = convertToRoom(s);
            JButton b = new JButton();

            //??
            b.setBackground(Color.white);
            b.setContentAreaFilled(true);
            b.setBorder(BorderFactory.createBevelBorder(1));
            //??

            b.setBounds(room.getX(), room.getY(), room.getH(), room.getW());
            b.addMouseListener(new moveMouseListener(player, nebs));
            bPane.add(b, 4);
            room.setButton(b);
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
        JLabel icon = set.getCardIcon();
        bPane.remove(icon);
        bPane.revalidate();
        bPane.repaint();
    }
    public static void flipCard(Set set){
        removeCard(set);
        placeCard(set, set.getCard().getImage());
    }

    public static void placeCardBacks(HashMap<String, Set> sets){
        for(Set set : sets.values()){
            placeCard(set, "images/CardBack.jpg");
        }
    }
   
    public static void placeShots(HashMap<String, Set> sets) {
        for (Set set : sets.values()) {
            for (Shot shot : set.getShotList()) {
                // Add a scene card to this room
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
        JLabel playerLabel = new JLabel();
        ImageIcon pIcon = new ImageIcon(player.getIcon());
        playerLabel.setIcon(pIcon);
        playerLabel.setBounds(x, y, 46, 46);
        playerLabel.setOpaque(true);
        player.setLabel(playerLabel);
        bPane.add(playerLabel, new Integer(1));
    }
    public static void removePlayer(Player player) {
        JLabel rm = player.getLabel();
        bPane.remove(rm);
        bPane.revalidate();
        bPane.repaint();
    }

    public static void placeInTrailer(Player player){

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
        pIcon.setBounds(icon.getIconWidth()+10, 210,150, 20);
        bPane.add(pIcon, new Integer(2));

        //JButton pRank;
        pRank = new JButton("Rank: "+player.getRank());
        pRank.setBackground(Color.white);
        pRank.setBounds(icon.getIconWidth()+10, 240,150, 20);
        bPane.add(pRank, new Integer(2));

        //JButton pCredits;
        pCredits = new JButton("Credits: "+player.getCredits());
        pCredits.setBackground(Color.white);
        pCredits.setBounds(icon.getIconWidth()+10, 270,150, 20);
        bPane.add(pCredits, new Integer(2));

        //JButton pDollars;
        pDollars = new JButton("Dollars: "+player.getDollars());
        pDollars.setBackground(Color.white);
        pDollars.setBounds(icon.getIconWidth()+10, 300,150, 20);
        bPane.add(pDollars, new Integer(2));

        //JButton pRehearsalTokens;
        pRehearsalTokens = new JButton("Rehears Toks: "+player.getRehearsalTokens());
        pRehearsalTokens.setBackground(Color.white);
        pRehearsalTokens.setBounds(icon.getIconWidth()+10, 330,150, 20);
        bPane.add(pRehearsalTokens, new Integer(2));

        //JButton pRoom;
        pRoom = new JButton("Room: "+player.getRoom());
        pRoom.setBackground(Color.white);
        pRoom.setBounds(icon.getIconWidth()+10, 330,150, 20);
        bPane.add(pRoom, new Integer(2));

        //JButton pRole;
        pRole = new JButton("Role: "+player.getRole());
        pRole.setBackground(Color.white);
        pRole.setBounds(icon.getIconWidth()+10, 360,150, 20);
        bPane.add(pRole, new Integer(2));

        //JButton pOnCard;
        pOnCard = new JButton("OnCard?: "+player.isOnCard());
        pOnCard.setBackground(Color.white);
        pOnCard.setBounds(icon.getIconWidth()+10, 390,150, 20);
        bPane.add(pOnCard, new Integer(2));
    }
}
