/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pac_man;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Diego Delgado
 */
public class Inicio extends JFrame{
    
    int WIDTH = 800, HEIGTH = 600;
    
    JPanel mainScreen;
    JLabel Fondo;
    ImageIcon Fondo_main;
    KeyListen manejaKey;
    
    public Inicio(){
        super(".: Ventana Principal :.");
        
        mainScreen = new JPanel(null); 
        
        manejaKey = new KeyListen();
        
        Fondo = new JLabel();
        Fondo.setBounds(0, 0, WIDTH, HEIGTH);
        Fondo_main = new ImageIcon("Imaganes Pacman\\plis.gif");
        Fondo_main = new ImageIcon(Fondo_main.getImage().getScaledInstance(WIDTH, HEIGTH, Image.SCALE_DEFAULT));
        Fondo.setIcon(Fondo_main);
        
        mainScreen.add(Fondo);

        this.addKeyListener(manejaKey);
        
        this.add(mainScreen);
        /*Container con = getContentPane();
        con.setLayout(new FlowLayout());
        con.add(mainScreen);*/
        
        setSize(WIDTH, HEIGTH);
        setResizable(false);
        setVisible(true);
    }
    
    
    private class KeyListen extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent ke) {
            if(ke.getKeyCode()== KeyEvent.VK_ENTER){
                dispose();
                Menu men = new Menu();
                men.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }
    
    
}
