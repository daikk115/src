/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

import java.util.ArrayList;

/**
 *
 * @author daidv
 */
public class MyRobot {
    public int x, y, speed;
    public String fileImage;
    public int battery = 1000; // số bước còn lại mà pin có thể đi.
    public int numberstep = 0;  // số bước đã đi tính từ chỗ xạc pin
    public ArrayList<SubCell> listStep = new ArrayList<>();
    
    public MyRobot(){
        this.x = 0;
        this.y = 0;
        this.speed = 100;
        this.fileImage = "src/hecstt2/image/robot.png"; //getClass().getResource("../images/robot.png");
        
    }
    
    public MyRobot(int x, int y){
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.fileImage = "src/hecstt2/image/robot.png";
    }
    
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public boolean checkInListStep(int x, int y){
        for (SubCell sub : this.listStep) {
            if(sub.column == x && sub.row == y) return true;
        }
        return false;
    }
}
