package objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{
    public OBJ_Sword_Normal(GamePanel gp){
        super(gp);
        name = "Normal Sword";
        down1 = setup("/object/sword_normal");
        attackValue = 1; 
        description = "[" + name + "]\nAn old sword";
    }
}
