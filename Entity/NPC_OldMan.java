package Entity;

import java.util.Random;
import main.GamePanel;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp){

        super(gp);
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }
 
    public void getImage(){

        up1 = setup("/NPC/oldman_up_1");
        up2 = setup("/NPC/oldman_up_2");
        down1 = setup("/NPC/oldman_down_1");
        down2 = setup("/NPC/oldman_down_2");
        left1 = setup("/NPC/oldman_left_1");
        left2 = setup("/NPC/oldman_left_2");
        right1 = setup("/NPC/oldman_right_1");
        right2 = setup("/NPC/oldman_right_2");
    }

    public void setDialogue(){

        dialogue[0] = "Tepi sial!";
        dialogue[1] = "Apa hajat tuan hamba kemari?";
        dialogue[2] = "Dahulu patik merupakan seorang \nahli sihir yang handal," + 
                      "\nnamun kini usia patik sudah lanjut, \ntidaklah lagi terdaya untuk berkelana.";
        dialogue[3] = "Moga berjaya hendaknya anda \ndalam segala urusan.";
    }

    public void setAction(){

        actionLockCounter++;

        if(actionLockCounter == 120){

            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25){
                direction = "up";
            }else if(i >25 && i <= 50){
                direction = "down";
            }else if(i >50 && i <= 75){
                direction = "left";
            }else if(i >75 && i <= 100){
                direction = "right"; 
            }

            actionLockCounter = 0;
        }
        
    }

    public void speak(){
        //specific interaction only
        super.speak();
    }
}
