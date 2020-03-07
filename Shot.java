import java.util.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class Shot {
    int x;
    int y;
    JLabel icon;

    public JLabel getIcon() {
        return icon;
    }

    public void setIcon(JLabel icon) {
        this.icon = icon;
    }

    public Shot(int x, int y) {
        this.x = x;
        this.y = y;
        this.icon = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
