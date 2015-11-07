/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

/**
 *
 * @author daidv
 */
public class MyRobot {
    public int x, y, speed;
    public String fileImage;
    
    public MyRobot(){
        this.x = 0;
        this.y = 0;
        this.speed = 100;
        this.fileImage = "src/hecstt2/image/Insect-robot.png";
    }
    
    public MyRobot(int x, int y){
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.fileImage = "src/hecstt2/image/Insect-robot.png";
    }
    
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
}
