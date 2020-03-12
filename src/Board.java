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
import javax.swing.border.EtchedBorder;

public class Board extends JFrame{

    // JLabels
    static JLabel boardlabel;
    static JLayeredPane bPane;

    static JLabel mLabel;
    JLabel pIcon;
    JLabel pRank;
    JLabel pCredits;
    JLabel pDollars;
    JLabel pRehearsalTokens;
    JLabel pRoom;
    JLabel pRole;
    JLabel pDays;

    //JButtons
    JButton bMove;
    JButton bTakeRole;
    JButton bRehearse;
    JButton bAct;
    JButton bUpgrade;
    JButton bEnd;
    JButton bBack;

    ImageIcon icon;

    ArrayList<String> playerDice = new ArrayList<>(Arrays.asList("b1.png", "c1.png", "g1.png", "o1.png", "p1.png", "r1.png", "v1.png", "w1.png", "y1.png"));

    String[][] dice = {{"b1.png", "b2.png", "b3.png", "b4.png", "b5.png", "b6.png"},
            {"c1.png", "c2.png", "c3.png", "c4.png", "c5.png", "c6.png"},
            {"g1.png", "g2.png", "g3.png", "g4.png", "g5.png", "g6.png"},
            {"o1.png", "o2.png", "o3.png", "o4.png", "o5.png", "o6.png"},
            {"p1.png", "p2.png", "p3.png", "p4.png", "p5.png", "p6.png"},
            {"r1.png", "r2.png", "r3.png", "r4.png", "r5.png", "r6.png"},
            {"v1.png", "v2.png", "v3.png", "v4.png", "v5.png", "v6.png"},
            {"w1.png", "w2.png", "w3.png", "w4.png", "w5.png", "w6.png"},
            {"y1.png", "y2.png", "y3.png", "y4.png", "y5.png", "y6.png"}};

    public String[][] getDice() { return dice; }
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

        Deadwood deadwood = Deadwood.getInstance();


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

        mLabel = new JLabel("MENU");
        mLabel.setBounds(icon.getIconWidth()+63,5,100,20);
        bPane.add(mLabel,new Integer(2));

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

            Deadwood deadwood = Deadwood.getInstance();
            Player player = Deadwood.getInstance().getCurrentPlayer();
            HashMap<String, Set> sets = deadwood.getSets();

            if (e.getSource() == bMove){
                System.out.println("Move is Selected\n");
                createMoveButtons();
            }
            else if (e.getSource() == bTakeRole){
                createRoleButtons();
            }
            else if (e.getSource() == bRehearse){

                if (player.getRole() != null) {
                    //update player budget
                    deadwood.setPlayerBudget(player);

                    //check if rehearsing is needed
                    if (player.getRehearsalTokens()  >= deadwood.getBudget()-1) {
                        popUpMessage("No need to rehearse again. Why don't you try acting?");
                    } else {
                        player.setRehearsalTokens(player.getRehearsalTokens() + 1);

                        //grammar
                        if (player.getRehearsalTokens() == 1) {
                            popUpMessage("You now have 1 rehearsal token.");
                        } else {
                            popUpMessage("You now have " + player.getRehearsalTokens() + " rehearsal tokens.");
                        }

                        //update rehearsal tokens view before turn ends
                        clearPlayerStats();
                        showPlayerStats(player);

                        deadwood.endTurn();
                    }
                } else {
                    popUpMessage("You have no role to rehearse for!");
                }
            }
            else if (e.getSource() == bAct){
                if(player.hasMoved() == false && player.getRole() != null){
                    //act
                    player.act(player, deadwood.getCurrentSet(player), deadwood.getPlayers());

                    //update player stats before turn ends
                    clearPlayerStats();
                    showPlayerStats(player);
                    //endTurn
                    Deadwood.endTurn();
                }
                else{
                    popUpMessage("You can't act right now");
                }
                System.out.println("Acting is Selected\n");
            }
            else if (e.getSource() == bUpgrade){
                if (player.getRoom().equalsIgnoreCase("office")) {
                    deadwood.upgrade();
                    int playerNum = 0;
                    for (Player p : deadwood.getPlayers()) {
                        if (p == player) {
                            break;
                        }
                        playerNum++;
                    }

                    player.setIcon("images/dice/" + dice[playerNum][player.getRank()-1]);
                    removePlayer(player);
                    placePlayerInRoom(player, "office");

                    clearPlayerStats();
                    showPlayerStats(player);
                } else {
                    popUpMessage("You must be in the Casting Office to upgrade");
                }

                System.out.println("Upgrade is Selected\n");
            }
            else if (e.getSource() == bEnd){
                deadwood.endTurn();
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

                    createTurnButtons();
                    Deadwood.getInstance().endTurn();
                }
            }
            for(Role role : this.set.getCard().getRoles()){
                if(e.getSource() == role.getButton()){
                    System.out.println("clicked "+role.getName()+" oncard");

                    removeRoleButtons(set, player);

                    //player takes role
                    removePlayer(player);
                    placePlayer(role.getX()+set.getX(), role.getY()+set.getY(), player);
                    player.setRole(role);
                    player.setOnCard(true);
                    role.setFilled(true);
                    role.setLabel(player.getLabel());

                    createTurnButtons();
                    Deadwood.getInstance().endTurn();
                }
            }
            if (e.getSource() == bBack) {
                removeRoleButtons(set, player);
                createTurnButtons();
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
        bPane.remove(mLabel);
        bPane.remove(bBack);
        bPane.revalidate();
        bPane.repaint();
    }
    public boolean hasAvailableRole(Player player){

        boolean found = false;
        Room room = convertToRoom(player.getRoom());
        if(player.getRole() == null && room instanceof Set && ((Set) room).getCard() != null) {

            Set set = Deadwood.getInstance().getSets().get(player.getRoom());
            for (Role role : set.getRoles()) {
                if ((!role.isFilled()) && player.getRank() >= role.getRank()) {
                    found = true;
                }
            }
            for (Role role : set.getCard().getRoles()) {
                if ((!role.isFilled()) && player.getRank() >= role.getRank()) {
                    found = true;
                }
            }
        }
        return found;
    }
    public void createRoleButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        Room room = convertToRoom(player.getRoom());
        if(player.getRole() == null && room instanceof Set && ((Set) room).getCard() != null){
            if(hasAvailableRole(player)){
                removeTurnButtons();

                mLabel = new JLabel("ROLES");
                mLabel.setBounds(icon.getIconWidth()+63,5,100,20);
                bPane.add(mLabel,new Integer(2));

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

                bBack = new JButton("Back");
                bBack.setBackground(Color.gray);
                bBack.setOpaque(true);
                bBack.setBorderPainted(false);
                bBack.setBounds(icon.getIconWidth()+10, y, 150, 20);
                bBack.addMouseListener(new roleMouseListener(set,player));
                bPane.add(bBack,2);
            }
            else{
                popUpMessage("You can't take a role right now!");
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
            if (e.getSource() == bBack) {
                removeMoveButtons();
                createTurnButtons();
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
        bPane.remove(mLabel);
        bPane.remove(bBack);
        bPane.revalidate();
        bPane.repaint();
    }
    public void createMoveButtons(){
        Player player = Deadwood.getInstance().getCurrentPlayer();
        if(player.getRole() == null && player.hasMoved() == false){
            removeTurnButtons();

            mLabel = new JLabel("ROOMS");
            mLabel.setBounds(icon.getIconWidth()+60,5,100,20);
            bPane.add(mLabel,new Integer(2));
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
            bBack = new JButton("Back");
            bBack.setBackground(Color.gray);
            bBack.setOpaque(true);
            bBack.setBorderPainted(false);
            bBack.setBounds(icon.getIconWidth()+10, y, 150, 20);
            bBack.addMouseListener(new moveMouseListener(player, nebs));
            bPane.add(bBack,2);
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

        //System.out.println("Shots: " + set.getCurrentShots());

        Deadwood deadwood = Deadwood.getInstance();
        for(Shot shot : set.getShotList()){
            if(shot.getIcon() != null){
                bPane.remove(shot.getIcon());
                bPane.revalidate();
                bPane.repaint();
                shot.setIcon(null);
                break;
            }
        }
    }

    public void placePlayer(int x, int y, Player player) {
        removePlayer(player);

        player.setX(x);
        player.setY(y);
        JLabel playerLabel = new JLabel();
        ImageIcon pIcon = new ImageIcon(player.getIcon());
        playerLabel.setIcon(pIcon);
        playerLabel.setBounds(x, y, 43, 43);
        playerLabel.setOpaque(true);
        player.setLabel(playerLabel);
        bPane.add(playerLabel, new Integer(4));
    }
    public void removePlayer(Player player) {
        if(player.getLabel() != null){
            JLabel rm = player.getLabel();
            bPane.remove(rm);
            bPane.revalidate();
            bPane.repaint();
        }

        if(player.getRoom() != null){
            Room previous = convertToRoom(player.getRoom());
            for(Slot slot : previous.getSlots()){
                if(slot.getPlayer() == player){
                    slot.setPlayer(null);
                }
            }
        }


    }

    public void placePlayerInRoom(Player player, String roomString){



        Room room = convertToRoom(roomString);

        for(Slot slot : room.getSlots()){
            if(slot.getPlayer() == null){
                slot.setPlayer(player);
                placePlayer(slot.getX(),slot.getY(), player);
                break;
            }
        }
        player.setRoom(roomString);

        //flip card in set if necessary
        if(room instanceof Set){
            Set set = (Set)room;
            if(set.getCard() != null && set.isCardFlipped() == false){
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
        bPane.remove(pDays);
        //bPane.remove(pOnCard);
        bPane.revalidate();
        bPane.repaint();
    }
    public void showPlayerStats(Player player) {
        //pIcon = new JButton("Icon: "+player.getIcon().substring(12));
        pIcon = new JLabel(player.getName() + "'s turn");
        pIcon.setBackground(Color.white);
        pIcon.setBorder(BorderFactory.createEtchedBorder(0));
        pIcon.setBounds(icon.getIconWidth() + 10, 240, 150, 20);
        bPane.add(pIcon, new Integer(2));

        //JButton pRank;
        pRank = new JLabel("Rank: " + player.getRank());
        pRank.setBackground(Color.white);
        pRank.setBorder(BorderFactory.createEtchedBorder(0));
        pRank.setBounds(icon.getIconWidth() + 10, 270, 150, 20);
        bPane.add(pRank, new Integer(2));

        //JButton pCredits;
        pCredits = new JLabel("Credits: " + player.getCredits());
        pCredits.setBackground(Color.white);
        pCredits.setBorder(BorderFactory.createEtchedBorder(0));
        pCredits.setBounds(icon.getIconWidth() + 10, 300, 150, 20);
        bPane.add(pCredits, new Integer(2));

        //JButton pDollars;
        pDollars = new JLabel("Dollars: " + player.getDollars());
        pDollars.setBackground(Color.white);
        pDollars.setBorder(BorderFactory.createEtchedBorder(0));
        pDollars.setBounds(icon.getIconWidth() + 10, 330, 150, 20);
        bPane.add(pDollars, new Integer(2));

        //JButton pRehearsalTokens;
        pRehearsalTokens = new JLabel("Rehears Toks: " + player.getRehearsalTokens());
        pRehearsalTokens.setBackground(Color.white);
        pRehearsalTokens.setBorder(BorderFactory.createEtchedBorder(0));
        pRehearsalTokens.setBounds(icon.getIconWidth() + 10, 360, 150, 20);
        bPane.add(pRehearsalTokens, new Integer(2));

        //JButton pRoom;
        pRoom = new JLabel("Room: " + player.getRoom());
        pRoom.setBackground(Color.white);
        pRoom.setBorder(BorderFactory.createEtchedBorder(0));
        pRoom.setBounds(icon.getIconWidth() + 10, 390, 150, 20);
        bPane.add(pRoom, new Integer(2));

        //JButton pRole;
        if (player.getRole() == null) {
            pRole = new JLabel("Role: None");
        } else {
            pRole = new JLabel("Role: " + player.getRole().getName());
        }
        pRole.setBackground(Color.white);
        pRole.setBorder(BorderFactory.createEtchedBorder(0));
        pRole.setBounds(icon.getIconWidth() + 10, 420, 150, 20);
        bPane.add(pRole, new Integer(2));

        pDays = new JLabel("Day: "+Deadwood.getInstance().getDays());
        pDays.setBackground(Color.red);
        pDays.setOpaque(true);
        pDays.setBorder(BorderFactory.createEtchedBorder(0));
        pDays.setBounds(icon.getIconWidth() + 10, 480, 150, 20);
        bPane.add(pDays, new Integer(2));
    }

    public static void endOfDayDialog(){
        JOptionPane.showMessageDialog(null, "Day ended.");
    }

    public static void endOfGameDialog(){
        JOptionPane.showMessageDialog(null,"The game is over!");
        String scores = "";
        for(Player player : Deadwood.getInstance().getPlayers()){
            scores += player.getName()+"'s final score: "+(5*player.getRank()+player.getDollars()+player.getCredits())+"\n";
        }
        JOptionPane.showMessageDialog(null, scores);
    }
    public static void popUpMessage(String s){
        JOptionPane.showMessageDialog(null, s);
    }


    public void makeRoomSlots(){
        Room train = convertToRoom("train station");
        for(int i = 0; i < 6; i++){
            train.getSlots().add(new Slot(train.getX()+40*i,train.getY()+train.getH()));
        }
        train.getSlots().add(new Slot(train.getX(),train.getY()+train.getH()+45));
        train.getSlots().add(new Slot(train.getX()+40,train.getY()+train.getH()+45));

        Room saloon = convertToRoom("saloon");
        for(int i = 0; i < 8; i++){
            saloon.getSlots().add(new Slot(saloon.getX()+42*i,saloon.getY()+saloon.getH()));
        }

        Room bank = convertToRoom("bank");
        for(int i = 0; i < 8; i++){
            bank.getSlots().add(new Slot(bank.getX()+42*i,bank.getY()+bank.getH()));
        }

        Room main = convertToRoom("main street");
        for(int i = 1; i < 9; i++){
            main.getSlots().add(new Slot((main.getX()+main.getW())-42*i,main.getY()+main.getH()));
        }

        Room church = convertToRoom("church");
        for(int i = 0; i < 8; i++){
            church.getSlots().add(new Slot(church.getX()+42*i,church.getY()+church.getH()));
        }

        Room secret = convertToRoom("secret hideout");
        for(int i = 0; i < 8; i++){
            secret.getSlots().add(new Slot(secret.getX()+42*i,secret.getY()+secret.getH()));
        }

        Room hotel = convertToRoom("hotel");
        for(int i = 0; i < 8; i++){
            hotel.getSlots().add(new Slot(hotel.getX()+42*i,hotel.getY()+hotel.getH()));
        }

        Room general = convertToRoom("general store");
        for(int i = 1; i < 9; i++){
            general.getSlots().add(new Slot((general.getX()+general.getW())-42*i,general.getY()+general.getH()));
        }

        Room jail = convertToRoom("jail");
        for(int i = 0; i < 4; i++){
            jail.getSlots().add(new Slot(jail.getX()+42*i,jail.getY()+jail.getH()));
        }
        for(int i = 0; i < 4; i++){
            jail.getSlots().add(new Slot(jail.getX()+42*i,jail.getY()+jail.getH()+45));
        }

        Room ranch = convertToRoom("ranch");
        for(int i = 0; i < 4; i++){
            ranch.getSlots().add(new Slot(ranch.getX()+42*i,ranch.getY()+ranch.getH()));
        }
        for(int i = 0; i < 4; i++){
            ranch.getSlots().add(new Slot(ranch.getX()+42*i,ranch.getY()+ranch.getH()+45));
        }

        Room trailer = convertToRoom("trailer");
        for(int i = 0; i < 4; i++){
            trailer.getSlots().add(new Slot(trailer.getX()+45*i,trailer.getY()+30));
        }
        for(int i = 0; i < 4; i++){
            trailer.getSlots().add(new Slot(trailer.getX()+45*i,trailer.getY()+30+45));
        }

        Room office = convertToRoom("office");
        for(int i = 0; i < 4; i++){
            office.getSlots().add(new Slot(office.getX()+45*i,office.getY()+30));
        }
        for(int i = 0; i < 4; i++){
            office.getSlots().add(new Slot(office.getX()+45*i,office.getY()+30+45));
        }
    }
}
