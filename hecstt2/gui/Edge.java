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

public class Edge {
    public SubCell start;
    public SubCell end;
    public Edge(SubCell start, SubCell end){
        this.start = new SubCell(start.column, start.row);
        this.end = new SubCell(end.column, end.row);
    }
}
