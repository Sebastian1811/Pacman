/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pac_man;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.text.Font.font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
//import static pac_man.Game.WIDTH;

/**
 *
 * @author Diego Delgado
 */
public class Jugar extends JFrame{
    
    int WIDTH = 800, HEIGTH = 600;
    int TIEMPODEJUEGO = 120000;
    JPanel juego; // panel donde se presenta el juego
    JLabel FondoJuego;
    ImageIcon FondoImage;
    String player;
    String puntos;
    JLabel nombre;
    JLabel score;
    JLabel pause;
    JLabel TiempoJuego;
    JLabel ElementosScreen[][] = new JLabel[22][19];
    boolean Pause = false;
    boolean Gameover = false;
    int juegologico[][] = new int[22][19];
    int juegologico2[][] = new int[22][19];
    int posx,posy;
    int Score;
    int arriba=0;
    int abajo = 0;
    int der = 0;
    int izq = 0;
    Timer comida;
    Timer power;
    boolean eaten = false;
    boolean Firstfood = true;
    boolean eaten2 = false;
    boolean Firstfood2 = true;
    KeyListen keymanager;
    Actionlisten actionmanager;
    ghost ghostes [] = new ghost[2];
    ghost ghost1 = new ghost (33,10,8);
    ghost ghost2 = new ghost (31,1,17);
    JLabel matAux [][] = new JLabel[22][19];
    Timer timer; // timer del movimiento del pacman
    Timer TiempodeJuego;
    Timer Persecucion;
    int contadorseg =0;
    int contadormin =0;
    public Jugar(){
        super(".: Juego :.");
        juego = new JPanel(null);
        posx = 1;
        posy = 1;
        ghostes [0]= ghost1;
        ghostes[1] = ghost2;
        // Se cargan los valores iniciales a las matrices de juego
        juegologico = Board(1);
        juegologico2 = BoardG();
        // posiciones de los personajes 
        juegologico[posx][posy]=3;
        juegologico2[ghost1.posx][ghost1.posy] = ghost1.Nombre;
        juegologico2[ghost2.posx][ghost2.posy] = ghost2.Nombre;
        /// Listeners de la ventana
        keymanager = new KeyListen();
        actionmanager = new Actionlisten();  
       /// se rellenan las matrices de labels///
        for(int i =0;i< 22;i++){
            for(int j=0;j< 19;j++){
                ElementosScreen[i][j] = new JLabel();
            }
        }
        for(int i= 0; i< 22;i++){
            for(int j= 0; j< 19;j++){
                matAux[i][j] = new JLabel();
            }   
        }
        // Se prepara el fondo de pantalla del juego
        FondoJuego = new JLabel();
        FondoJuego.setBounds(0, 0, WIDTH, HEIGTH);
        FondoImage = new ImageIcon("Imaganes Pacman\\fondo.png");
        FondoImage = new ImageIcon(FondoImage.getImage().getScaledInstance(WIDTH, HEIGTH, Image.SCALE_DEFAULT));
        FondoJuego.setIcon(FondoImage);
        FondoJuego.setVisible(true);
       // Se carga la informacion de la matriz de juego en los labels
        for(int i =0;i< 22;i++){
            for(int j=0;j< 19;j++){
            ElementosScreen[i][j].setIcon(new ImageIcon("Imaganes Pacman\\"+juegologico[i][j]+".png"));
            ElementosScreen[i][j].setBounds(50+(j*20),50+(i*20),20, 20);
            ElementosScreen[i][j].setVisible(true); 
            juego.add(ElementosScreen[i][j],0);
            }
        }
        for(int i =0;i< 22;i++){
            for(int j=0;j< 19;j++){
            matAux[i][j].setIcon(new ImageIcon("Imaganes Pacman\\"+juegologico2[i][j]+".png"));
            matAux[i][j].setBounds(50+(j*20),50+(i*20),20, 20);
            matAux[i][j].setVisible(true); 
            juego.add(matAux[i][j],0);
            }
        }
        /// Imagen que indica la pausa del juego
        pause = new JLabel();
        pause.setBounds(450, 300, 250, 177);
        pause.setIcon(new ImageIcon("Imaganes Pacman\\pause.gif"));
        pause.setVisible(false);
        /// Cronometro que controla cuanto tiempo lleva jugando
        TiempoJuego = new JLabel();
        TiempoJuego.setBounds(450, 100, 100, 50);
        TiempoJuego.setVisible(true);
        // Se agregan todos los elementos al Jpanel de juego
        juego.add(FondoJuego,0);
        juego.add(pause,0);
        juego.add(TiempoJuego,0);
        // Se agrega el panel a la ventana
        this.add(juego);
        // Propiedades de la ventana 
        setSize(800, 600);
        setResizable(false);
        setVisible(true);
        // Arranca el juego
        mover();
        //// Version de fantasma con hilos ////
       ghost1.start();
       ghost2.start();
       ///////////////////////////////////////
       /// en caso de querer usar timer descongele esto////
       /*ghost1.t1.start(); // descongelar codigo en funciones Power, Killpacman
       ghost2.t1.start();*/
       /**************************************************/
       TiempodeJuego = new Timer(1000,actionmanager); /// setea timers necesarios
       TiempodeJuego.start();
       Persecucion = new Timer(5000,actionmanager);
       GenerateFood();// comienza generacion de comida y poderes
       generatePower();
    }
    
    public void mover(){ // Inicia el movimiento de pacman
        juego.requestFocusInWindow(); // para que la ventana vuelva a tener el foco no borrar
        juego.addKeyListener(keymanager);
        timer = new Timer(250, actionmanager);
        timer.start();    // comienza pacaman a moverse
    }
    
    public int [][] Board(int op){// matrices iniciales de juego, mundo de pacman
        int [][] matAux = new int [22][19];
        int mataux[][] = {
            {2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2, 2, 2},
            {2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2},
            {2 ,4 ,2 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,2 ,4 ,2},
            {2 ,4 ,2 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,2 ,4 ,2},
            {2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2},
            {2 ,4 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,4 ,2},
            {2 ,4 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,4 ,2},
            {2 ,2 ,2 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,2 ,2 ,2},
            {1 ,1 ,1 ,2 ,4 ,2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,4 ,2 ,1 ,1 ,1},
            {2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,0 ,0 ,0 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2},
            {2 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,0,4 ,0 ,2 ,4 ,4 ,4 ,4 ,4 ,4 , 2},
            {2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2},
            {1 ,1 ,1 ,2 ,4 ,2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,4 ,2 ,1 ,1 ,1},
            {2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2},
            {2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2},
            {2 ,4 ,2 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,4 ,2 ,2 ,4 ,2},
            {2 ,4 ,4 ,2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2 ,4 ,4 ,2},
            {2 ,2 ,4 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,4 ,2 ,2},
            {2 ,4 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,2 ,4 ,4 ,4 ,4 ,2},
            {2 ,4 ,2 ,2 ,2 ,2 ,2 ,2 ,4 ,2 ,4 ,2 ,2 ,2 ,2 ,2 ,2 ,4 ,2},
            {2 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,4 ,2},
            {2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2},        
        };  
        return mataux;
    }
    public int [][] BoardG(){ // mundo de los fantasmas jajajaj
        int [][] matAux = new int [22][19];
        int mataux[][] = {
            {2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2, 2, 2},
            {2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2},
            {2 ,0 ,2 ,2 ,0 ,2 ,2 ,2 ,0 ,0 ,0 ,2 ,2 ,2 ,0 ,2 ,2 ,0 ,2},
            {2 ,0 ,2 ,2 ,0 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,0 ,2 ,2 ,0 ,2},
            {2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2},
            {2 ,0 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,0 ,2},
            {2 ,0 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,0 ,2},
            {2 ,2 ,2 ,2 ,0,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,0 ,2 ,2 ,2 ,2},
            {0 ,0 ,0 ,2 ,0,2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2 ,0 ,2 ,0 ,0 ,0},
            {2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,0 ,0 ,0 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2},
            {2 ,0 ,0 ,0 ,0 ,0 ,0 ,2 ,0,0 ,0 ,2 ,0 ,0 ,0 ,0 ,0 ,0 ,2},
            {2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2},
            {0 ,0 ,0 ,2 ,0 ,2 ,0 ,0 ,0 ,0 ,0,0 ,0 ,2 ,0 ,2 ,0 ,0 ,0},
            {2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2},
            {2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2},
            {2 ,0 ,2 ,2 ,0 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,0 ,2 ,2 ,0 ,2},
            {2 ,0 ,0 ,2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2 , 0,0 ,2},
            {2 ,2 ,0 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,0 ,2 ,2},
            {2 ,0 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,2 ,0 ,0 ,0 ,0 ,2},
            {2 ,0 ,2 ,2 ,2 ,2 ,2 ,2 ,0 ,2 ,0 ,2 ,2 ,2 ,2 ,2 ,2 ,0 ,2},
            {2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2},
            {2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2},        
        };  
        return mataux;
    }
    public void KillPacman(){ // Valida si pacman muere solo hay una vida ajajja
        for(int i = 0; i< ghostes.length; i++){
            if(ghostes[i].Modocaza){ // si estan en modo caza me matan
                if(ghostes[i].posx == posx && ghostes[i].posy == posy){
                    
                    /*ghost1.t1.stop(); -----> si muero creo que no es necesario parar a los fantasmas
                    ghost2.t1.stop();*/
                    timer.stop(); // detengo mi timer
                    TiempodeJuego.stop(); // no se juega mas
                    Gameover = true; // fin del juego
                }
            }else{ // si no estan cazando puedo comerlos
                if(ghostes[i].posx == posx && ghostes[i].posy == posy){
                    ghostes[i].Dead= true; // murio en version hilos
                    ///ghostes[i].t1.stop(); ----> version de timers 
                    ghostes[i].Nombre= 0; // ya no existe el fantasma
                    ghostes[i].reservaNombre= 0; 
                    Score+=30; // 30 puntos por matar un ghost
                    juegologico2[ghostes[i].posx][ghostes[i].posy] = 0;//actualiza
                }
            }
        }
    }
    
    public void GenerateFood(){ // Genera comida en partes random del mapa cada 6 sg
        Actionlisten SpawnComida = new Actionlisten(); // en el actionListener esta la generacion de la comida
         comida = new Timer(6000, SpawnComida);
         comida.start(); // cuenta el tiempo para generar
    }
    public void generatePower(){ // Genera el poder representado por la moneda
        Actionlisten spawnpower = new Actionlisten();
         power = new Timer(15000, spawnpower); // Igual que la comida pero con poderes jajajaj
         power.start();// cuenta el tiempo para generar
    }
    public void Comer(){ /// si pacman pasa encima de una cereza
        if(juegologico[posx][posy]==6){ // 6 es una cereza
            eaten = true; // se comio la cereza
            for(int i = 0; i<22;i++){
                for(int j = 0; j<19;j++){
                    if( juegologico2[i][j]==33 ){ //busca los fantasmsa y los convierte en vulnerables
                        juegologico2[i][j]=10; // actualizo el mundo de los fantasmas
                        ghost1.Nombre = 10; // ahora es un fantasma que se puede comer
                        ghost1.Modocaza = false;// ya no esta en modo cazador
                    }else if(juegologico2[i][j]==31){
                        juegologico2[i][j]=10;
                        ghost2.Nombre = 10;
                        ghost2.Modocaza = false;
                    }
                }
            }
            Persecucion.start(); // inicia el tiempo de persecucion para que pacman coma
            painting();
        }
    }
    public void ComerPoints(){ /// si pacman pasa encima de un punto implementacion de puntaje jaaj lol ( el gordo lleva 12 horas intentandolo :v)
        if(juegologico[posx][posy]==4){ // 4 es un punto 
            Score+=10; // 10 puntos cada circulo
            System.out.println(""+Score);
            painting();
        }
    }
    public void Finpersecucion(){ // acaba la persecucion
        for(int i = 0; i<ghostes.length;i++){
                ghostes[i].Modocaza = true; // vuelven a cazar
                ghostes[i].Nombre = ghostes[i].reservaNombre; // vuelve su nombre original
                juegologico2[ghostes[i].posx][ghostes[i].posy] = ghostes[i].reservaNombre;
            }
    }
    public void Power(){ // activa el poder luego de comerlo
        double Probabilidad = (Math.random()); // genera la probabilidad
        if(juegologico[posx][posy]==9){
            eaten2 = true; // comio el poder 
            if(Probabilidad<0.3){// se supone que sale el 30 porciento de las veces
                System.out.println("MAS TIEMPO"); // ---> deberia dar un aviso mejor quizas una imagen jaja
                TIEMPODEJUEGO += 3000; /// ---> se regalan 3 sg
            }else if(Probabilidad>=0.9){ // se supone que solo ocurre el 10 porciento de las veces es instawin
                for(int i = 0; i<22;i++){
                    for(int j = 0; j<19;j++){
                        if( juegologico[i][j]==4){
                            juegologico[i][j]=0; // todos los puntos se van
                            Score+=1; // se suma uno al score
                        }
                    }
                }
                painting(); // actualiza
                /*version de timers****/
                //ghost1.t1.stop(); --> descongelar si quiere timers
                /*ghost2.t1.stop();*/
                for(int i = 0; i< ghostes.length; i++){
                    ghostes[i].Dead = true; // ---> muerte en version hilos
                }
                timer.stop(); // detiene a pacman
                TiempodeJuego.stop(); // para el tiempo
                Gameover = true; // se acaba el juego
            }if(Probabilidad<0.5){ // el cincuenta po ciento de las veces detiene a un fantasma y lo borra
                int random = (int)(Math.random()*1); // un fantasma random
                //ghostes[random].t1.stop(); ---> version  de timers
                ghostes[random].Dead=true; // version de hilos
                juegologico2[ghostes[random].posx][ghostes[random].posy] = 0; // actualiza el mundo de fantasmas 
            }
        }
    }
    
    private class KeyListen extends KeyAdapter{
        public void keyPressed(KeyEvent ke) { // controla el movimiento por teclado de pacman
            if(!Gameover){
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_W:
                        izq=0;
                        abajo=0;
                        der =0;
                        if(juegologico[posx-1][posy]==2){
                        }else{
                            juegologico[posx][posy] =0;
                            posx-=1;
                            Comer();
                            Power();
                            ComerPoints();
                            juegologico[posx][posy] =3;
                            arriba =1;
                            KillPacman();
                            painting();
                        }             
                        break;
                    case KeyEvent.VK_S:
                        izq=0;
                        arriba=0;
                        der =0;
                        if(juegologico[posx+1][posy]==2){
                        }else{
                            juegologico[posx][posy] =0;
                            posx+=1;
                             Comer();
                             ComerPoints();
                             Power();
                            juegologico[posx][posy] =3;
                            abajo =1;
                            KillPacman();
                            painting();
                        }    
                    break;
                    case KeyEvent.VK_A:
                        arriba=0;
                        abajo=0;
                        der =0;
                        if(juegologico[posx][posy-1]==2){
                        }else{
                            juegologico[posx][posy] =0;
                            posy-=1;
                            Comer();
                            ComerPoints();
                            Power();
                            juegologico[posx][posy] =3;
                            izq =1;
                            KillPacman();
                            painting();
                        }
                    break;    

                    case KeyEvent.VK_D:
                            if(juegologico[posx][posy+1]==2){
                            }else{
                            juegologico[posx][posy] =0;
                            posy+=1;
                            Power();
                            ComerPoints();
                            Comer();
                            juegologico[posx][posy] =3;
                            der =1;
                            KillPacman();
                            
                            painting();
                        }
                    break;
                    case KeyEvent.VK_ESCAPE: // la pausa va full con timers debo arreglarla con hilos NO HAY PAUSA CON HILOS
                        if(!Pause){
                            /*ghost1.t1.stop(); // pausa de timers de fantasmas
                            ghost2.t1.stop();*/
                            timer.stop(); // detiene pacman
                            TiempodeJuego.stop();//para time de juego
                            Pause = true;
                            pause.setVisible(true);
                        }else if(Pause){
                            /*ghost1.t1.start();
                            ghost2.t1.start();*/
                            timer.start();
                            TiempodeJuego.start();
                            Pause = false;
                            pause.setVisible(false);
                        }
                    break;
                } 
            }     
        }     
    }
    
    
    public void painting(){ // Actualiza la matriz de Labels con la informacion de la matriz de juego

        for(int i =0;i< 22;i++){
            for(int j=0;j< 19;j++){
               ElementosScreen[i][j].setIcon(new ImageIcon("Imaganes Pacman\\"+juegologico[i][j]+".png"));
               ElementosScreen[i][j].setBounds(50+(j*20),50+(i*20),20, 20);
               ElementosScreen[i][j].setVisible(true); 
               juego.add(ElementosScreen[i][j],0);
            }
        }
        for(int i =0;i< 22;i++){
            for(int j=0;j< 19;j++){
                matAux[i][j].setIcon(new ImageIcon("Imaganes Pacman\\"+juegologico2[i][j]+".png"));
                matAux[i][j].setBounds(50+(j*20),50+(i*20),20, 20);
                matAux[i][j].setVisible(true);
                juego.add(matAux[i][j],0);
            }
        }
    }
    
    private class Actionlisten implements ActionListener{

            public void actionPerformed(ActionEvent ae) {
                if(ae.getSource() == timer){ /// Controla el movimiento automatico de pacman  
                    if(der == 1){
                        if(juegologico[posx][posy+1]==2){    
                        }else{
                            juegologico[posx][posy] =0;
                            posy += 1;
                            Comer();
                            Power();
                            ComerPoints();
                            juegologico[posx][posy] =3;
                            der = 1;
                            ComerPoints();
                            KillPacman();
                            painting();
                        }
                    }else if(izq == 1){
                        if(juegologico[posx][posy-1]==2){
                        }else{
                            juegologico[posx][posy] =0;
                            posy-=1;
                            Comer();
                            ComerPoints();
                            Power();
                            juegologico[posx][posy] =3;
                            izq =1;
                            KillPacman();
                            painting();
                        }    
                    }else if(arriba == 1){
                        if(juegologico[posx-1][posy]==2){    
                        }else{
                            juegologico[posx][posy] =0;
                            posx-=1;
                            Comer();
                            Power();
                            ComerPoints();
                            juegologico[posx][posy] =3;
                            arriba =1;
                            KillPacman();
                            painting();
                        }    
                    }else if(abajo== 1){
                        if(juegologico[posx+1][posy]==2){
                        }else{
                            juegologico[posx][posy] =0;
                            posx+=1;
                            Comer();
                            Power();
                            ComerPoints();
                            juegologico[posx][posy] =3;
                            abajo =1;
                            KillPacman();
                            painting();
                        }    
                    } 
                }else if(ae.getSource()== TiempodeJuego){ // Controla el tiempo jugado
                    if(TIEMPODEJUEGO!=0){
                        contadorseg++;
                        String mensaje = contadormin+":"+contadorseg;
                        TiempoJuego.setForeground(Color.WHITE);
                        TiempoJuego.setFont(new Font(mensaje, Font.PLAIN, 20));
                        TiempoJuego.setText(mensaje);
                        if(contadorseg==60){
                            contadormin++;
                            contadorseg=0;
                        }
                        TIEMPODEJUEGO-=1000; 
                    }else if(TIEMPODEJUEGO==0){// Si no hay mas tiempo pausa todo y se acaba el juego
                        ghost1.t1.stop();
                        ghost2.t1.stop();
                        timer.stop();
                        TiempodeJuego.stop();
                        Gameover = true;
                    }
                }else if(ae.getSource() == comida){ // controla el spawn de comida
                    int Randomx, Randomy;
                    if(Firstfood){ // para generar la primera comida de todas
                         do{
                            Randomx = (int)(Math.random()*22);
                            Randomy = (int)(Math.random()*19); // valida que no se genere en paredes
                        }while(juegologico[Randomx][Randomy] ==2 ||juegologico[Randomx][Randomy] ==1);
                            juegologico[Randomx][Randomy] = 6;
                            Firstfood = false;
                    }
                    if(eaten){ //para generar las siguientes
                        do{
                            Randomx = (int)(Math.random()*22);
                            Randomy = (int)(Math.random()*19);
                        }while(juegologico[Randomx][Randomy] ==2);
                        juegologico[Randomx][Randomy] = 6;
                        eaten = false;
                    }
                }else if(ae.getSource() == power){ // controla el spawn de poderes 
                    int Randomx, Randomy;
                    if(Firstfood2){ // genera el primer poder de todos
                         do{
                            Randomx = (int)(Math.random()*22);
                            Randomy = (int)(Math.random()*19);
                        }while(juegologico[Randomx][Randomy] ==2); // valida que no se genere en muros 
                            juegologico[Randomx][Randomy] = 9;
                            Firstfood2 = false;
                    }
                    if(eaten2){ // para el resto de poderes
                        do{
                            Randomx = (int)(Math.random()*22);
                            Randomy = (int)(Math.random()*19);
                        }while(juegologico[Randomx][Randomy] ==2);
                        juegologico[Randomx][Randomy] = 9;
                        eaten2 = false;
                    }
                }else if(ae.getSource()== Persecucion){// controla la persecucion
                    Persecucion.stop(); 
                    Finpersecucion();// regresa los fantasmas a mod normal
                }   
            }
        } 
    public class ghost extends Thread{ // los fantasmas son hilos
        int posx,posy; // posiciones que van a ocupar en el mundo fantasma
        int Nombre,reservaNombre; // nombre y copia de nombre
        boolean Modocaza = true; // inician en modo cazador jjaja
        boolean Dead = false; // no estan muertos :v
        Action a = new Action(); // para version de timers
        Timer t1 = new Timer(250,a); // timer que controla el movimiento y matar
        public ghost(int nombre, int posx, int posy){
            Nombre = nombre; // el nombre es para el nombre de la imagen
            reservaNombre = nombre; // copia para salir del modo persecucion
            this.posx = posx;
            this.posy = posy;
        }

        public void movimiento(){ // moviminto de fantasma (muy basico bastante estupido)
            int random = (int)(Math.random()*4);
            switch(random){
                case 0:
                    
                    if(juegologico2[this.posx-1][this.posy]==2){
                    }else{
                         juegologico2[this.posx][this.posy] =0;
                        this.posx-=1;
                        juegologico2[this.posx][this.posy] = Nombre;
                       
                        }           
                    break;
                case 1: 
                    
                     if(juegologico2[posx+1][posy]==2){
                     }else{
                        juegologico2[posx][posy] =0;
                        posx+=1;
                        juegologico2[posx][posy] =Nombre;
                        }    
                    break;
                case 2:
                    if(juegologico2[this.posx][this.posy-1]==2){
                    }else{
                        juegologico2[this.posx][this.posy] =0;
                        posy-=1;
                        juegologico2[this.posx][this.posy] =Nombre;
                        }
                    break;
                case 3:
                    if(juegologico2[posx][posy+1]==2){
                    }else{
                        juegologico2[posx][posy] =0;
                        posy+=1;
                        juegologico2[posx][posy] =Nombre;
                    }
                    break;
            }
        }
        private class Action implements ActionListener{ /// controla el timer
            @Override
            public void actionPerformed(ActionEvent ae) {
                movimiento(); // mueve
                painting();// actualiza
            }
        }
        
        public void run(){ /// version de hilos
            while(true){
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Jugar.class.getName()).log(Level.SEVERE, null, ex);
                }
                painting();
                movimiento();
                if(Gameover || Dead){ // si muere o se acaba el juego adios hilos
                    break;
                }
            }
        }
   }
}
