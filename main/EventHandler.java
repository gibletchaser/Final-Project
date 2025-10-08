package main;

import java.awt.Rectangle;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][];

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        for(int col = 0; col < gp.maxWorldCol; col++){
            for(int row = 0; row < gp.maxWorldRow; row++){
                eventRect[col][row] = new EventRect();
                eventRect[col][row].x = 0;
                eventRect[col][row].y = 0;
                eventRect[col][row].width = 2;
                eventRect[col][row].height = 2;
                eventRect[col][row].eventRectDefaultX = 0;
                eventRect[col][row].eventRectDefaultY = 0;
            }
        }
    }

    public void checkEvent(){
        // NEW! Add a question event at coordinate (30, 10)
        if(hit(17, 27, "up") == true){questionEvent(18, 29);}
    }

    public boolean hit(int col, int row, String reqDirection){
        boolean hit = false;

        if(col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow){
            return false;
        }

        if(eventRect[col][row].eventDone == true){
            return false;
        }

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y; 
        eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;

        if(gp.player.solidArea.intersects(eventRect[col][row])){
            if(gp.player.direction.equals(reqDirection) || reqDirection.equals("any")){
                hit = true;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    
    // NEW! Question event
    public void questionEvent(int col, int row){
        // Ask a question
        gp.questionDialogue.askQuestion(
            "What is 5 + 3?",  // The question
            "8"                 // The correct answer
        );
        
        eventRect[col][row].eventDone = true; // Player can only answer once
    }
}
