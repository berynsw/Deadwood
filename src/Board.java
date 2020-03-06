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

public class Board extends JFrame {

    // JLabels
    static JLabel boardlabel;
    static JLayeredPane bPane;


    static JLabel playerlabel;
    static JLabel mLabel;


    //JButtons
    JButton bAct;
    JButton bRehearse;
    JButton bMove;
    // JLayered Pane

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
        ImageIcon icon =  new ImageIcon("images/board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));

        // Set the size of the GUI
        setSize(icon.getIconWidth()+200,icon.getIconHeight());



        // Add a dice to represent a player.
        // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon("r2.png");
        playerlabel.setIcon(pIcon);
        //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
        playerlabel.setBounds(114,227,46,46);
        playerlabel.setVisible(false);
        bPane.add(playerlabel,new Integer(3));

        // Create the Menu for action buttons
        mLabel = new JLabel("MENU");
        mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
        bPane.add(mLabel,new Integer(2));

        // Create Action buttons
        bAct = new JButton("ACT");
        bAct.setBackground(Color.white);
        bAct.setBounds(icon.getIconWidth()+10, 30,100, 20);
        bAct.addMouseListener(new boardMouseListener());

        bRehearse = new JButton("REHEARSE");
        bRehearse.setBackground(Color.white);
        bRehearse.setBounds(icon.getIconWidth()+10,60,100, 20);
        bRehearse.addMouseListener(new boardMouseListener());

        bMove = new JButton("MOVE");
        bMove.setBackground(Color.white);
        bMove.setBounds(icon.getIconWidth()+10,90,100, 20);
        bMove.addMouseListener(new boardMouseListener());

        // Place the action buttons in the top layer
        bPane.add(bAct, new Integer(2));
        bPane.add(bRehearse, new Integer(2));
        bPane.add(bMove, new Integer(2));
    }

    // This class implements Mouse Events
    class boardMouseListener implements MouseListener{

        // Code for the different button clicks
        public void mouseClicked(MouseEvent e) {

            if (e.getSource()== bAct){
                playerlabel.setVisible(true);
                System.out.println("Acting is Selected\n");
            }
            else if (e.getSource()== bRehearse){
                System.out.println("Rehearse is Selected\n");
            }
            else if (e.getSource()== bMove){
                System.out.println("Move is Selected\n");
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


    public static int getPlayerNum(){
        String[] options = {"2 players", "3 players", "4 players", "5 players", "6 players", "7 players", "8 players"};
        int[] nums = {2,3,4,5,6,7,8};
        int option =  JOptionPane.showOptionDialog(null, "How many players?", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
        return nums[option];
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






}
