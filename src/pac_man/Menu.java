/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pac_man;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Diego Delgado
 */
public class Menu extends JFrame{
    
    int WIDTH = 800, HEIGTH = 600;
    
    JPanel Menu;
    JButton MenuButtons[];
    ImageIcon Fondo_main;
    JLabel Fondo_menu;
    ImageIcon menuImage;
    Actionlisten actionmanage;
    
    public Menu(){
        super(".: Menu :.");
        
        Menu = new JPanel();
        Menu.setLayout(null);
        
        actionmanage = new Actionlisten();
        
        MenuButtons = new JButton[3];
        for(int i = 0; i<MenuButtons.length;i++){
           MenuButtons[i] = new JButton(); 
        }
        
        Fondo_menu = new JLabel();
        Fondo_menu.setBounds(0, 0, WIDTH, HEIGTH);
        menuImage = new ImageIcon("Imaganes Pacman\\menu.jpg");
        menuImage = new ImageIcon(menuImage.getImage().getScaledInstance(WIDTH, HEIGTH, Image.SCALE_DEFAULT));
        Fondo_menu.setIcon(menuImage);
        
        MenuButtons[0].setText("Jugar");
        MenuButtons[1].setText("Scores");
        MenuButtons[2].setText("Exit");
        
        for(int i = 0; i<MenuButtons.length;i++){
           MenuButtons[i].setBounds((250),(i+3)*50, 100, 40);
           MenuButtons[i].setVisible(true);
           MenuButtons[i].setBackground(Color.white);
           MenuButtons[i].addActionListener(actionmanage);
           Menu.add(MenuButtons[i]);
        }
        
        Menu.add(Fondo_menu);
        
        this.add(Menu);
        
        setSize(WIDTH, HEIGTH);
        setResizable(false);
        setVisible(true);
    }
    
    
    private class Actionlisten implements ActionListener{

        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == MenuButtons[0]){               
                dispose();
                Jugar juego = new Jugar();
                juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }else if(ae.getSource() == MenuButtons[1]){
                
            }else if(ae.getSource() == MenuButtons[2]){
                
                  System.exit(0); 
            }
        }
    }
    
}
