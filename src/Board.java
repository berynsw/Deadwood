/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.List;
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
    //List<JButton> bAdj = new ArrayList<>();


    JButton bAdj1;
    JButton bAdj2;
    JButton bAdj3;
    JButton bAdj4;

    JButton[] bAdj = {bAdj1, bAdj2, bAdj3, bAdj4};

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

        Deadwood deadwood = Deadwood.getInstance();

        // Create the JLayeredPane to hold the display, cards, dice and buttons
        bPane = getLayeredPane();

        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon = new ImageIcon("images/board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));

        // Set the size of the GUI
        setSize(icon.getIconWidth() + 200, icon.getIconHeight());

        //Initialize menu
        initMenu();
    }

    public void initMenu() {

        Deadwood deadwood = Deadwood.getInstance();

        ImageIcon icon = new ImageIcon("images/board.jpg");

        // Create the Menu for action buttons
        if (mLabel == null) {
            mLabel = new JLabel("MENU");
            mLabel.setBounds(icon.getIconWidth() + 80, 0, 100, 20);
            bPane.add(mLabel, new Integer(2));

        }
        // Create Action buttons
        bAct = new JButton("ACT");
        bAct.setBackground(Color.white);
        bAct.setBounds(icon.getIconWidth() + 50, 30, 100, 20);
        bAct.addMouseListener(new boardMouseListener());

        bRehearse = new JButton("REHEARSE");
        bRehearse.setBackground(Color.white);
        bRehearse.setBounds(icon.getIconWidth() + 50, 60, 100, 20);
        bRehearse.addMouseListener(new boardMouseListener());

        bMove = new JButton("MOVE");
        bMove.setBackground(Color.white);
        bMove.setBounds(icon.getIconWidth() + 50, 90, 100, 20);
        bMove.addMouseListener(new boardMouseListener());

        // Place the action buttons in the top layer
        bPane.add(bAct, new Integer(2));
        bPane.add(bRehearse, new Integer(2));
        bPane.add(bMove, new Integer(2));
    }

    public void listRooms() {

        Deadwood deadwood = Deadwood.getInstance();
        clearMenu();
        int yPos = 30;

        ImageIcon icon = new ImageIcon("images/board.jpg");
        bPane = getLayeredPane();
        List<String> neibs = deadwood.getNeighbors(deadwood.getCurrentPlayer().getRoom(), deadwood.getSets(),
                deadwood.getTrailer(), deadwood.getOffice());
        //create neighbors
        bAdj1 = new JButton(neibs.get(0));
        bAdj1.setName(neibs.get(0));
        bAdj1.setBackground(Color.white);
        bAdj1.setBounds(icon.getIconWidth() + 40, yPos, 100, 20);
        bAdj1.addMouseListener(new boardMouseListener());
        bPane.add(bAdj1, new Integer(2));
        yPos += 30;
        bAdj[0] = bAdj1;

        bAdj2 = new JButton(neibs.get(1));
        bAdj1.setName(neibs.get(1));
        bAdj2.setBackground(Color.white);
        bAdj2.setBounds(icon.getIconWidth() + 40, yPos, 100, 20);
        bAdj2.addMouseListener(new boardMouseListener());
        bPane.add(bAdj2, new Integer(2));
        yPos += 30;
        bAdj[1] = bAdj2;

            bAdj3 = new JButton(neibs.get(2));
            bAdj1.setName(neibs.get(2));
            bAdj3.setBackground(Color.white);
            bAdj3.setBounds(icon.getIconWidth() + 40, yPos, 100, 20);
            bAdj3.addMouseListener(new boardMouseListener());
            bPane.add(bAdj3, new Integer(2));
            yPos += 30;
            bAdj[2] = bAdj3;

        if (neibs.size() > 3) {
            bAdj4 = new JButton(neibs.get(3));
            bAdj1.setName(neibs.get(3));
            bAdj4.setBackground(Color.white);
            bAdj4.setBounds(icon.getIconWidth() + 40, yPos, 100, 20);
            bAdj4.addMouseListener(new boardMouseListener());
            bPane.add(bAdj4, new Integer(2));
            yPos += 30;
            bAdj[3] = bAdj4;
        }
    }

    public void clearMenu() {
        //bPane.remove(mLabel);
        bPane.remove(bAct);
        bPane.remove(bRehearse);
        bPane.remove(bMove);
        bPane.revalidate();
        bPane.repaint();
    }

    public void clearMoves() {

        //bPane.remove(mLabel);
        bPane.remove(bAdj1);
        bPane.remove(bAdj2);
        bPane.remove(bAdj3);

        if (bAdj4 != null) {
            bPane.remove(bAdj4);
        }

        bPane.revalidate();
        bPane.repaint();
    }

    // This class implements Mouse Events
    class boardMouseListener implements MouseListener {

        Deadwood deadwood = Deadwood.getInstance();

        // Code for the different button clicks
        public void mouseClicked(MouseEvent e) {
            //ImageIcon icon = new ImageIcon("images/board.jpg");
            bPane = getLayeredPane();
            addName(deadwood.getCurrentPlayer());
            if (e.getSource() == bAct) {
                //playerlabel.setVisible(true);
                System.out.println("Acting is Selected\n");
            } else if (e.getSource() == bRehearse) {
                System.out.println("Rehearse is Selected\n");
            } else if (e.getSource() == bMove) {
                clearMenu();
                addName(deadwood.getCurrentPlayer());
                listRooms();
                System.out.println("Move is Selected\n");
            } else if (e.getSource() == bAdj1) {
                String selectedRoom = ((JButton)e.getSource()).getText();
                System.out.println(selectedRoom);
                deadwood.getCurrentPlayer().setRoom(selectedRoom);
                clearMoves();
                initMenu();
            } else if (e.getSource() == bAdj2) {
                String selectedRoom = ((JButton)e.getSource()).getText();
                System.out.println(selectedRoom);
                deadwood.getCurrentPlayer().setRoom(selectedRoom);
                clearMoves();
                initMenu();
            } else if (e.getSource() == bAdj3) {
                String selectedRoom = ((JButton)e.getSource()).getText();
                System.out.println(selectedRoom);
                deadwood.getCurrentPlayer().setRoom(selectedRoom);
                clearMoves();
                initMenu();
            } else if (e.getSource() == bAdj4) {
                String selectedRoom = ((JButton)e.getSource()).getText();
                System.out.println(selectedRoom);
                deadwood.getCurrentPlayer().setRoom(selectedRoom);
                clearMoves();
                initMenu();
            }
        }

        public void removeRooms(List rooms) {

        }

        public void listAllRooms(Player player) {
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


    public static int getPlayerNum() {
        String[] options = {"2 players", "3 players", "4 players", "5 players", "6 players", "7 players", "8 players"};
        int[] nums = {2, 3, 4, 5, 6, 7, 8};
        int option = JOptionPane.showOptionDialog(null, "How many players?", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return nums[option];
    }

    public static void placeCard(int x, int y, String file) {
        // Add a scene card to this room
        JLabel cardlabel = new JLabel();
        ImageIcon cIcon = new ImageIcon(file);
        cardlabel.setIcon(cIcon);
        cardlabel.setBounds(x, y, 205, 115);
        cardlabel.setOpaque(true);
        // Add the card to the lower layer
        bPane.add(cardlabel, new Integer(1));
    }

    public static void placeCardBacks(HashMap<String, Set> sets) {
        for (Set set : sets.values()) {
            placeCard(set.getX(), set.getY(), "images/CardBack.jpg");
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

    public static void trailerPlayers(List<Player> players) {
        //(x,y) of trailer
        int x = 991;
        int y = 320;
        //pixels to increment x as we add more players
        int increment = 5;
        int count = 1;
        for (Player p : players) {
            //if we have more than 4 players on a line, move to next line
            if (count > 4) {
                increment = 5;
                y += 50;
                x = 991;
                count = 0;
            }
            placePlayer(x + increment, y, p);
            //increment players placed
            count++;
            //increment x pos
            increment += 50;
        }
    }

    public static void removePlayer(Player player) {
        JLabel rm = player.getLabel();
        bPane.remove(rm);
        bPane.revalidate();
        bPane.repaint();
    }

    public static void addName(Player player) {
        Deadwood deadwood = Deadwood.getInstance();
        ImageIcon icon = new ImageIcon("images/board.jpg");
        if (player != null) {

            bPane.remove(mLabel);
            bPane.revalidate();
            bPane.repaint();
            mLabel = new JLabel(deadwood.getCurrentPlayer().getName() + "'s Turn");
            mLabel.setBounds(icon.getIconWidth() + 50, 0, 100, 20);
            bPane.add(mLabel, new Integer(2));
        }
    }
}