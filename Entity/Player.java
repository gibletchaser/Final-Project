package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import objects.OBJ_Key;
import objects.OBJ_Shield_Wood;
import objects.OBJ_Sword_Normal;

import java.awt.Graphics2D;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(0, 0, 48, 48);
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    public void setDefaultValues(){

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        //PLAYER STATUS 
        level = 1;
        maxLife = 6;
        life = maxLife; 
        strength = 1; //higher the strengths, higher the damage
        dexterity = 1; //higher the dexterity, lesser damage receive
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp); 
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack(); //total damage depends on strength and weapon
        defense = getDefense(); //total defense depends on dexterity and shield
    }

    public void setDefaultPosition(){

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }

    public void restoreLifeandMana(){

        life = maxLife; 
    }

    public void setItems(){

        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage(){

        up1 = setup("/walk_sprite/ziq_up_1");
        up2 = setup("/walk_sprite/ziq_up_2");
        down1 = setup("/walk_sprite/ziq_down_1");
        down2 = setup("/walk_sprite/ziq_down_2");
        left1 = setup("/walk_sprite/ziq_left_1");
        left2 = setup("/walk_sprite/ziq_left_2");
        right1 = setup("/walk_sprite/ziq_right_1");
        right2 = setup("/walk_sprite/ziq_right_2");
    }


    public void update(){

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){

            if(keyH.upPressed == true){
                direction = "up";
            }else if(keyH.downPressed == true){
                direction = "down";
            }else if(keyH.leftPressed == true){
                direction = "left";
            }else if(keyH.rightPressed == true){
                direction = "right";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK EVENT
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed = false;


            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false){

                switch (direction) {
                    case "up": worldY -= speed; break;
                    case"down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                }else if(spriteNum == 2){
                    spriteNum = 1;
                }
            spriteCounter = 0;
            }
        }

        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.playSE(6);
        }
    }

    public void pickUpObject(int i){
        if(i != 999){
        }

    }

    public void interactNPC(int i){
        if(i != 999){

            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
 
        switch(direction){
            case "up":
            if(spriteNum == 1){
                image = up1;
            }
            if(spriteNum == 2){
                image = up2;
            }
                break;
            case "down":
            if(spriteNum == 1){
                image = down1;
            }
            if(spriteNum == 2){
                image = down2;
            }
                break;
            case "left":
            if(spriteNum == 1){
                image = left1;
            }
            if(spriteNum == 2){
                image = left2;
            }
                break;
            case "right":
            if(spriteNum == 1){
                image = right1;
            }
            if(spriteNum == 2){
                image = right2;
            }
                break;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, null);
        }

    }
}
