package main;

import Entity.Entity;
import Entity.Player;
import Tiles.tileManager;
import environment.EnvironmentManager;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.omg.CORBA.Environment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
public class GamePanel extends JPanel implements Runnable{
    //SCREEN SETTING
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    //FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen; 
    Graphics2D g2;
    public boolean fullScreenOn = false;

    //FPS
    int FPS = 60;

    //SYSTEM
    tileManager tileM = new tileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    EnvironmentManager eManager = new EnvironmentManager(this);
    public String playerInput = "";
    public boolean waitingForInput = false;
    public String correctAnswer1 = "office123";
    public QuestionDialogue questionDialogue;
    Thread gameThread;

    //ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int gameOverState = 6;
    public final int inputState = 7;
    
    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        questionDialogue = new QuestionDialogue(this);
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        //playMusic(0);
        eManager.setup();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        //setFullScreen();
    }

    public void retry(){

        player.setDefaultPosition();
        player.restoreLifeandMana();
        aSetter.setNPC();
    }

    public void restart(){

        player.setDefaultValues();
        player.setItems();
        aSetter.setNPC();
        aSetter.setObject();
    }

    public void setFullScreen(){

        //GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //GET FULL SCREEN WIDTH AND HEIGHT 
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
    
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        double lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                drawToTempScreen(); //draw everything to the buffered image
                drawToScreen(); //draw the buffered image to the screen
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){

        if(gameState == playState){
            //PLAYER
            player.update();
            //NPC
            for(int i = 0;i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }

        }
        else if(gameState == pauseState){
            //NOTHING
        }
    }

    public void drawToTempScreen(){

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if(gameState == titleState){

            ui.draw(g2);
        }else{
            //TILE
            tileM.draw(g2);

            //ADD ENTITIES TO THE LIST
            entityList.add(player);
            for(int i = 0;i < npc.length; i++){
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }

            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2){
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
                
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST
            for(int i = 0; i < entityList.size(); i++){
                entityList.remove(i);
            }

            //ENVIRONMENT
            eManager.draw(g2);

            //UI
            ui.draw(g2);
        }

        //DEBUG
        if(keyH.showDebugText == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX" + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY" + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col" + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Row" + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;

            g2.drawString("Draw Time: " + passed, x, y);
        }

    
    }

    public void drawToScreen(){

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void checkAnswer(){
        if(playerInput.equalsIgnoreCase(correctAnswer1)){
            System.out.println("Correct!");
        }else{
            System.out.println("Wrong answer!");
            player.life--;
        }

        new Thread(() -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameState = playState;
        waitingForInput = false;
        playerInput = "";
        }).start();
    }

    public void playMusic(int i){
        
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }

    public void playSE(int i){

        se.setFile(i);
        se.play();
    }
}
