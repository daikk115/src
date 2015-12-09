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
public class SubCell {
    public int column, row;
    public boolean top, down, left, right;
    public boolean value; // là tường = false;
    public boolean valuebigcell; // là khối 4Cell vật cản = false;
    public boolean added; // đã thêm vào hàng listSTC chưa? nếu chưa thì là true;
    public boolean addedRNS;
    
    public SubCell(int x, int y){
        this.top = true;
        this.down = true;
        this.left = true;
        this.right = true;
        this.value = true;
        this.valuebigcell = true;
        this.column = x;
        this.row = y;
        this.added = false;
        this.addedRNS = false;
    }
    
}
