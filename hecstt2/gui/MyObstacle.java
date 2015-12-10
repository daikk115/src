/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

import java.awt.Rectangle;
import java.util.Random;
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
                runRamdom();
                break;
            }
        }

    }

    /**
     * kiểm tra xem thử vật cản và robot có bị giao nhau hay không.
     *
     * @param xRobot
     * @param yRobot
     * @param xObstacle
     * @param yObstacle
     * @return
     */
    public boolean getFlag(int xRobot, int yRobot, int xObstacle, int yObstacle) {
        Rectangle a = new Rectangle(xObstacle, yObstacle, this.frame.mapconfig.cell, this.frame.mapconfig.cell);
        Rectangle b = new Rectangle(xRobot, yRobot, this.frame.mapconfig.cell, this.frame.mapconfig.cell);
        return a.intersects(b);
    }

    public void runVertical() {
        boolean key = true;
        int height = this.frame.mapconfig.height - this.frame.mapconfig.cell;
        while (true) {

            if (this.y < height && key && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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
            if (this.y > 0 && !key && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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
            if (this.x < width && key && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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
            if (this.x > 0 && !key && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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
                if (checkNotOver(x, y) && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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
                if (checkNotOver(x, y) && !getFlag(frame.robot.x, frame.robot.y, x, y)) {
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

    public void runRamdom() {
        Random rd = new Random();
        while (true) {
            int cas = rd.nextInt(4);
            System.out.println(cas);
            switch (cas) {
                case 0: {
                    int n = x + frame.mapconfig.cell;
                    for (int i = x + 2; i <= n; i += 2) {
                        if (!checkNotOver(i, y) || getFlag(frame.robot.x, frame.robot.y, i, y)) {
                            break;
                        }
                        try {
                            this.x = i;
                            MyObstacle.sleep(20);
                            this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                                    this.frame.mapconfig.cell);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MyGraphics.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }   
                    break;
                }
                case 1: {
                    int n = y + frame.mapconfig.cell;
                    for (int j = y + 2; j <= n; j += 2) {
                        if (!checkNotOver(x, j) || getFlag(frame.robot.x, frame.robot.y, x, j)) {
                            break;
                        }
                        try {
                            this.y = j;
                            MyObstacle.sleep(20);
                            this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                                    this.frame.mapconfig.cell);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MyGraphics.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
                case 2: {
                    int n = y - frame.mapconfig.cell;
                    for (int j = y - 2; j >= n; j -= 2) {
                        if (!checkNotOver(x, j) || getFlag(frame.robot.x, frame.robot.y, x, j)) {
                            break;
                        }
                        try {
                            y = j;
                            MyObstacle.sleep(20);
                            this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                                    this.frame.mapconfig.cell);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MyGraphics.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
                case 3: {
                    int n = x - frame.mapconfig.cell;
                    for (int i = x - 2; i >= n; i -= 2) {
                        if (!checkNotOver(i, y) || getFlag(frame.robot.x, frame.robot.y, i, y)) {
                            break;
                        }
                        try {
                            x = i;
                            MyObstacle.sleep(20);
                            this.frame.repaint(this.x, this.y, this.frame.mapconfig.cell,
                                    this.frame.mapconfig.cell);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MyGraphics.class.getName()).log(
                                    Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
                default: {
                    System.out.println("DEFAULT");
                    break;
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
