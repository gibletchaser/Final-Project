package Tiles;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Graphics2D;

public class tileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public tileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/Aje_Gampang.txt");
    }

    public void getTileImage(){

        setup(0, "office_wall", false);
        setup(1, "wall", true);
        setup(2, "water", true);
        setup(3, "woodTile", false);
        setup(4, "wall", true);
        setup(5, "carpet1", false);
        setup(6, "carpetMiddle", false);
        setup(7, "carpetEndUp", false);
        setup(8, "carpetEndDown", false);
        setup(9, "carpetEndLeft", false);
        setup(10, "carpetEndRight", false);
        setup(11, "carpetUp", false);
        setup(12, "carpetTurnLeft", false);
        setup(13, "carpetTurnRight", false);
        setup(14, "carpetUpT", false);
        setup(15, "carpetDownT", false);
        setup(16, "carpetLeftT", false);
        setup(17, "carpetRightT", false);
    }

    public void setup(int index, String imageName, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Old version/" + imageName +".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        
        try{
            InputStream is = getClass() .getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0, row = 0;
            while( col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line  = br.readLine();

                while(col < gp.maxWorldCol){

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] =  num;
                    col++;
                }

                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch(Exception e){

        }
    }

    public void draw(Graphics g2){

        int worldCol = 0, worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX; 
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);

            }

            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }

        }   
    }
}
