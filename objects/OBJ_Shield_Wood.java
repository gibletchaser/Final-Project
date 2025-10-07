package objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity{
    public OBJ_Shield_Wood(GamePanel gp){
        super(gp);

        name = "Wood Shield";
        down1 = setup("/object/Shield_wood");
        defenseValue = 1;
        description = "[" + name + "]\nAn old shield";
    }
}
