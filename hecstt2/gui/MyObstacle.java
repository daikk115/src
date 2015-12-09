/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daidv
 */
public class MyObstacle extends Thread {

    MyGraphics frame;
    public int x, y, speed;
    public static String fileImage;
    public int keyMove = 0;

    public MyObstacle(MyGraphics frame) {
        this.frame = frame;
        this.x = 0;
        this.y = 100;
        this.speed = 100;
        this.fileImage = "src/hecstt2/image/download.jpg";
        this.start();
    }

    public MyObstacle(int x, int y, int keyMove, MyGraphics frame) {
        this.frame = frame;
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.fileImage = "src/hecstt2/image/download.jpg";
        this.keyMove = keyMove;
        this.start();
    }

    @Override
    public void run() {
        // thay đổi tạo độ robot.
        switch (keyMove) {
            case 0: {
                runHorizontal();
                break;
            }
            case 1: {
                runVertical();
                break;
            }
            case 2: {
                runCheo();
                break;
            }
            default: {
                runHorizontal();
                break;
            }
        }

    }

    /**
     * kiểm tra xem thử vật cản và robot có bị giao nhau hay không.
     *
     * @return
     */
    public boolean getFlag() {
        Rectangle a = new Rectangle(this.x, this.y, this.frame.mapconfig.cell, this.frame.mapconfig.cell);
        Rectangle b = new Rectangle(this.frame.robot.x, this.frame.robot.y, this.frame.mapconfig.cell, this.frame.mapconfig.cell);
        return a.intersects(b);
    }

    public void runVertical() {
        boolean key = true;
        int height = this.frame.mapconfig.height - this.frame.mapconfig.cell;
        while (true) {

            if (this.y < height && key) {
                this.y += 2;
                try {
                    MyObstacle.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                        this.frame.mapconfig.cell);
            } else {
                key = false;
            }
            if (this.y > 0 && !key) {
                this.y -= 2;
                try {
                    MyObstacle.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                        this.frame.mapconfig.cell);
            } else {
                key = true;
            }
        }
    }

    public void runHorizontal() {
        boolean key = true;
        int width = this.frame.mapconfig.width - this.frame.mapconfig.cell;
        while (true) {
            if (this.x < width && key) {
                this.x += 2;
                try {
                    MyObstacle.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                        this.frame.mapconfig.cell);
            } else {
                key = false;
            }
            if (this.x > 0 && !key) {
                this.x -= 2;
                try {
                    MyObstacle.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                        this.frame.mapconfig.cell);
            } else {
                key = true;
            }
        }
    }

    public void runCheo() {
        boolean key = true; // true là tăng, false, là giảm
        while (true) {
            if (key) {
                if (checkNotOver(x, y)) {
                    this.x += 2;
                    this.y += 2;
                    try {
                        MyObstacle.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                            this.frame.mapconfig.cell);
                } else {
                    key = false;
                    this.x -= 2;
                    this.y -= 2;
                }

            }
            if (!key) {
                if (checkNotOver(x, y)) {
                    this.x -= 2;
                    this.y -= 2;
                    try {
                        MyObstacle.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyObstacle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                            this.frame.mapconfig.cell);
                } else {
                    key = true;
                    this.x += 2;
                    this.y += 2;
                }
            }
        }
    }

    public boolean checkNotOver(int x, int y) {
        int height = this.frame.mapconfig.height - this.frame.mapconfig.cell;
        int width = this.frame.mapconfig.width - this.frame.mapconfig.cell;
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

}
