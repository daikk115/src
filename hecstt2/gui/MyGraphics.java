/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.gui;

/**
 *
 * @author daidv Hệ cơ sở tri thức - thầy Phạm Văn Hải
 */
import hecstt2.algorithm.OffLine;
import hecstt2.algorithm.OnLine;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Dai
 */
public class MyGraphics extends JFrame {

    private final JFileChooser fileChoose = new JFileChooser();
    private int chooser = -1;
    private File f;
    // danh sách đóng tương ứng với value = 3, ds mở tương ứng với value =2,
    // tường ứng với value =1;
    JTextArea textarea;

    private JPanel map = new JPanel();
    public MapConfig mapconfig = new MapConfig();

    private Rectangle screen;

    public MyRobot robot = new MyRobot(0, 0); // khởi tạo tọa độ robot, tạo độ

    public ArrayList<MyObstacle> listObstacles = new ArrayList<>();

    public BufferedImage img;
    public Graphics2D g2d;

    public SubCell[][] matrix;
    public ArrayList<Edge> listSTC = new ArrayList<>();

    private OffLine offline;
    private OnLine online;

    private boolean keySTC = false;

    public MyGraphics() {
        setTitle("BÀI TOÁN TÌM ĐƯỜNG ĐI BAO PHỦ");
        setLayout(new BorderLayout());
        this.screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        setLocation(screen.x, screen.y);
        setSize(screen.width, screen.height);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // tạo Menu
        MenuBar menu = new MenuBar();
        Menu file = new Menu("File");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    read();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi đọc bản đồ");
                    Logger.getLogger(MyGraphics.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        });
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    write();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi lưu bản đồ");
                    Logger.getLogger(MyGraphics.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        });
        file.add(open);
        file.add(save);
        menu.add(file);
        setMenuBar(menu);

        this.screen.height -= 20; // bớt 20 px của menu

        // tạo JPanel để xây dựng maps và khởi tạo mảng Subcell để mô hình hóa
        // maps thành ma trận
        mapconfig.ConfigSize(screen.width - 300, screen.height);
        // Khởi tạo image để gán cho giao diện bản đồ
        img = new BufferedImage(mapconfig.width + 1, mapconfig.height + 1,
                BufferedImage.TYPE_INT_RGB);
        GraphicsConfiguration gc = getGraphicsConfiguration();
        img = gc.createCompatibleImage(mapconfig.width + 1,
                mapconfig.height + 1, Transparency.TRANSLUCENT);

        this.g2d = (Graphics2D) img.createGraphics();
        // khởi tạo phần lưu trữ dữ liệu bản đồ
        matrix = new SubCell[mapconfig.numbercolumns][mapconfig.numberrows]; // các
        // sự
        // kiện
        // khi
        // thao
        // tác
        // trên
        // giao
        // diện
        // hoặc
        // load
        // file
        // sẽ
        // tác
        // động
        // đến
        // dữ
        // liệu
        // ma
        // trận
        // này
        for (int i = 0; i < mapconfig.numbercolumns; i++) {
            for (int j = 0; j < mapconfig.numberrows; j++) {
                matrix[i][j] = new SubCell(i, j);
            }
        }

        // add obstacles
        listObstacles.add(new MyObstacle(0, 100, 10, this, 1));
        listObstacles.add(new MyObstacle(0, 350, 10, this, 2));
        listObstacles.add(new MyObstacle(350, 0, 10, this, 3));
        listObstacles.add(new MyObstacle(250, 0, 10, this, 4));

        // map.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));
        map.setPreferredSize(new Dimension(screen.width - 300, screen.height));
        add(map, BorderLayout.EAST);
        mapconfig.ConfigLocation(map.getLocation());
        map.setDoubleBuffered(true);
        map.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int i = e.getX() / mapconfig.cell;
                int j = e.getY() / mapconfig.cell;
                if (i >= mapconfig.numbercolumns || j >= mapconfig.numberrows) {
                    return;
                }
                if (matrix[i][j].value) {
                    matrix[i][j].value = false;
                    repaint(i * mapconfig.cell, j * mapconfig.cell,
                            mapconfig.cell, mapconfig.cell);
                } else {
                    matrix[i][j].value = true;
                    repaint(i * mapconfig.cell, j * mapconfig.cell,
                            mapconfig.cell, mapconfig.cell);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        offline = new OffLine(this);
        online = new OnLine(this);

        // tạo JPanel để bỏ select heuristic vào
        JPanel left = new JPanel();
        left.setLayout(null);
        left.setPreferredSize(new Dimension(300, screen.height));
        left.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));

        // /////////////////////////////////////
        JButton run = new JButton("Chạy robot");
        run.setBounds(20, 20, 120, 35);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // gặp lỗi nhấn 2 lần tìm kiếm là lỗi??? xử lý sao và tại sao
                // lại lỗi
                offline.start();
            }
        });

        JButton searchstc = new JButton("Tìm cây khung");
        searchstc.setBounds(160, 20, 120, 35);
        searchstc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                keySTC = true;
                offline.checkAllBlock(mapconfig.numbercolumns,
                        mapconfig.numberrows);
                offline.searchSTC();
                repaint();
            }
        });

        // Online Algorithm
        JButton onlineBtn = new JButton("Online");
        onlineBtn.setBounds(20, 75, 120, 35);
        onlineBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                keySTC = true;
                online.start();
            }
        });

        JButton obsBtn = new JButton("show STC");
        obsBtn.setBounds(160, 75, 120, 35);
        obsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keySTC = !keySTC;
            }
        });

        // ///////////////////////////////////////
        JButton exit = new JButton("Thoát");
        exit.setBounds(20, 145, 120, 35);
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        textarea = new JTextArea(" ");
        textarea.setDragEnabled(true);
        JScrollPane text = new JScrollPane(textarea);
        text.setBounds(20, 20, 250, 100);

        JPanel top_left = new JPanel();
        top_left.setBorder(BorderFactory.createLineBorder(Color.green, 2, true));
        top_left.setLayout(null);
        top_left.setBounds(0, 0, 300, 250);

        JPanel middle_left = new JPanel();
        middle_left.setBorder(BorderFactory.createLineBorder(Color.green, 2));
        middle_left.setLayout(null);
        middle_left.setBounds(0, 250, 300, 200);
        middle_left.add(searchstc);
        middle_left.add(onlineBtn);
        middle_left.add(obsBtn);
        middle_left.add(run);

        JPanel bottom_left = new JPanel();
        bottom_left.setBorder(BorderFactory.createLineBorder(Color.green, 2));
        bottom_left.setLayout(null);
        bottom_left.setBounds(0, 450, 300, 250);
        bottom_left.add(exit);
        bottom_left.add(text);

        left.add(top_left);
        left.add(middle_left);
        left.add(bottom_left);

        add(left, BorderLayout.WEST);
        setVisible(true);
    }

    public void read() throws IOException {
        int c = -1;
        FileReader fr = null;
        BufferedReader br = null;
        chooser = fileChoose.showOpenDialog(null);
        if (chooser == JFileChooser.APPROVE_OPTION) {
            f = fileChoose.getSelectedFile();
            try {
                fr = new FileReader(f);
                br = new BufferedReader(fr);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MyGraphics.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
        // while(true)

        br.close();
        fr.close();
    }

    public void write() throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        chooser = fileChoose.showSaveDialog(null);
        if (chooser == JFileChooser.APPROVE_OPTION) {
            try {
                f = fileChoose.getSelectedFile();
                fw = new FileWriter(f);
                bw = new BufferedWriter(fw);
                // if(bw == null) return;
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(MyGraphics.class.getName()).log(Level.SEVERE,
                        null, ex);
            }

        }
    }

    /**
     * DFS tìm cây khung.
     */
    @Override
    public void paint(Graphics g) {
        g.dispose();
        super.paint(g);
        g = map.getGraphics();

        // gọi đói tượng Offline được khai báo ở đ
        drawMatrix(g);
        revalidate();
    }

    public void drawMatrix(Graphics g) {

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setBackground(Color.WHITE);
        // g2d.fillRect(0, 0, screen.width - 300, screen.height);
        // vẽ khối tường
        for (int i = 0; i < mapconfig.numbercolumns; i++) {
            for (int j = 0; j < mapconfig.numberrows; j++) {
                if (!matrix[i][j].value) {
                    g2d.setColor(Color.GRAY);
                    g2d.fillRect(i * mapconfig.cell, j * mapconfig.cell,
                            mapconfig.cell, mapconfig.cell);
                }else if (robot.checkInListStep(i, j)) {
                    g2d.setColor(Color.getHSBColor(0.43f, 0.46f, 0.98f));
                    g2d.fillRect(i * mapconfig.cell, j * mapconfig.cell,
                            mapconfig.cell, mapconfig.cell);
                } else {
                    g2d.setColor(Color.getHSBColor(0.424f, 0.611f, 0.706f));
                    g2d.fillRect(i * mapconfig.cell, j * mapconfig.cell,
                            mapconfig.cell, mapconfig.cell);
                }
            }
        }
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1));

        // vẽ các đường ngang dọc của bản đồ
        for (int i = 1; i <= mapconfig.numbercolumns; i++) { // tại sao không
            // +=mapconfig.cell,
            // lý do là để
            // vẽ đủ số
            // lượng lề thay
            // vì i<
            // mapconfig.width
            // đối khi thiếu
            // dòng cuối
            if (i % 2 == 0) {
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawLine(i * mapconfig.cell, 0, i * mapconfig.cell,
                    mapconfig.height);
        }
        for (int i = 0; i <= mapconfig.numberrows; i++) {
            if (i % 2 == 0) {
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.drawLine(0, i * mapconfig.cell, mapconfig.width, i
                    * mapconfig.cell); // 0 là x, i là y, tọa độ đề các x sang
            // phải, y đi xuống
        }

        // vẽ con robot
        g2d.drawImage(Toolkit.getDefaultToolkit().getImage(robot.fileImage),
                robot.x + 1, robot.y + 1, mapconfig.cell - 2,
                mapconfig.cell - 2, null);
        // File outputfile = new File("saveimg2.png");
        // ImageIO.write(img, "png", outputfile);
        Image tk = Toolkit.getDefaultToolkit().getImage(MyObstacle.fileImage);
        for (MyObstacle obstacle : listObstacles) {
            g2d.drawImage(tk, obstacle.x + 2, obstacle.y + 2,
                    mapconfig.cell - 4, mapconfig.cell - 4, null);
        }
        if (keySTC) {
            drawSTC(g2d);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, mapconfig.x, mapconfig.y, mapconfig.width,
                mapconfig.height, map);
    }

    public void drawSTC(Graphics2D g2d) {
        for (Edge e : this.listSTC) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(e.start.column * mapconfig.cell, e.start.row
                    * mapconfig.cell, e.end.column * mapconfig.cell, e.end.row
                    * mapconfig.cell);
        }
    }
}
