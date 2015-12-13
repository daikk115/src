/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

import java.awt.Point;

/**
 *
 * @author daidv
 */
public class MapConfig{
    public  int x, y, width, height, cell, numbercolumns, numberrows;
    
    /**
     * Đặt các cấu hình vào một khối cho tiện sử dụng.
     */
    public MapConfig(){
        
    }
    /**
     * Cấu hình chiều rộng(chiều ngang) , chiều cao và số ô cần chia để vẽ bản đồ
     * @param width
     * @param height
     */
    public void ConfigSize(int width, int height){
        this.width = width;
        this.height = height;
        
        // 10 | 20 | 30
        this.numbercolumns = 20; // cấu hình kích thước ở đây, phải à số chẵn vì số subcell phải chẵn
        this.cell = this.width / numbercolumns ;
        this.numberrows = this.height / this.cell;
        if(this.numberrows % 2 == 1){
            this.numberrows = this.numberrows - 1; // đưa nó về dạng số chẵn
        }
        this.width = this.numbercolumns * this.cell;
        this.height = this.numberrows * this.cell;
    }
    /**
     * Cấu hình vị trí của nó, để làm gì??? chưa biết
     * @param point
     */
    public void ConfigLocation(Point point){
        this.x = point.x;
        this.y = point.y;
    }
}
