package objects;


import Entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{


    public OBJ_Key(GamePanel gp){
        super(gp);

        name = "Key";
        down1 = setup("/object/key");
        description = "[" + name + "]\nAn old key";
    }
}
