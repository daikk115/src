package hecstt2.algorithm;

import hecstt2.gui.Edge;
import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class OnLine extends Algorithm {

    private SubCell robotCell = new SubCell(0, 0);
    private boolean keyWarning = true;

    public OnLine(MyGraphics frame) {
        super(frame);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        onlineSTC(null, new SubCell(1, 1));
    }

    /*
     * Online STC
     */
    public void onlineSTC(SubCell parentCell, SubCell currentCell) {

        ArrayList<SubCell> neighbors = neighbors(parentCell, currentCell);
        checkBlock2(currentCell);
        System.out.println("*Current Cell : " + currentCell.column + "x"
                + currentCell.row);

        for (int i = 0; i < neighbors.size(); i++) {
            if (!(matrix[neighbors.get(i).column][neighbors.get(i).row].valuebigcell)
                    || (matrix[neighbors.get(i).column][neighbors.get(i).row].added)
                    || checkInSTC(neighbors.get(i))) {
                continue;
            }
            if (!isLowBattery()) {
                constructST(currentCell, neighbors.get(i));
                System.out.println("Move foward " + i);
                move(currentCell, neighbors.get(i));
                System.out.println("here");
                onlineSTC(currentCell, neighbors.get(i));
            }else{
                if(keyWarning){
                    JOptionPane.showMessageDialog(null, "I feel not good. I must go back to recharge energy.", "Low battery!!!", JOptionPane.WARNING_MESSAGE, new ImageIcon("src/hecstt2/image/low_battery.png"));
                    keyWarning = false;
                }
                break;
            }
        }

        if (!isStart(currentCell)) {
            System.out.println("Move back");
            if (parentCell == null) {
                System.out.println("XXX");
            }
            move(currentCell, parentCell);
        } else {
            System.out.println("Finish");
        }
    }

    /*
     * Check Block, thay vi dung checkAllBlock khong dung! Y tuong: co vi tri
     * robot -> tim ra BigCell no dang nam, quet checkBlock,BigCell day va
     * neighbors cua no, gan gia tri boolean
     */
    public void checkBlock() {
        int bigCellCol = robotCell.column / 2;
        int bigCellRow = robotCell.row / 2;
        for (int i = (((bigCellCol * 2 - 2) >= 0) ? (bigCellCol * 2 - 2) : 0); i <= (((bigCellCol * 2 + 2) < mapconfig.numbercolumns) ? (bigCellCol * 2 + 2)
                : (bigCellCol * 2)); i += 2) {
            for (int j = (((bigCellRow * 2 - 2) >= 0) ? (bigCellRow * 2 - 2)
                    : 0); j <= (((bigCellRow * 2 + 2) < mapconfig.numberrows) ? (bigCellRow * 2 + 2)
                            : (bigCellRow * 2)); j += 2) {
                if (!(matrix[i][j].value && matrix[i + 1][j].value
                        && matrix[i][j + 1].value && matrix[i + 1][j + 1].value)) {
                    matrix[i][j].valuebigcell = false;
                    matrix[i][j + 1].valuebigcell = false;
                    matrix[i + 1][j].valuebigcell = false;
                    matrix[i + 1][j + 1].valuebigcell = false;
                }
            }
        }
    }

    public void checkBlock2(SubCell currentCell) {
        checkBlock();
        int bigCellCol = currentCell.column / 2;
        int bigCellRow = currentCell.row / 2;
        for (int i = (((bigCellCol * 2 - 2) >= 0) ? (bigCellCol * 2 - 2) : 0); i <= (((bigCellCol * 2 + 2) < mapconfig.numbercolumns) ? (bigCellCol * 2 + 2)
                : (bigCellCol * 2)); i += 2) {
            for (int j = (((bigCellRow * 2 - 2) >= 0) ? (bigCellRow * 2 - 2)
                    : 0); j <= (((bigCellRow * 2 + 2) < mapconfig.numberrows) ? (bigCellRow * 2 + 2)
                            : (bigCellRow * 2)); j += 2) {
                if (!(matrix[i][j].value && matrix[i + 1][j].value
                        && matrix[i][j + 1].value && matrix[i + 1][j + 1].value)) {
                    matrix[i][j].valuebigcell = false;
                    matrix[i][j + 1].valuebigcell = false;
                    matrix[i + 1][j].valuebigcell = false;
                    matrix[i + 1][j + 1].valuebigcell = false;
                }
            }
        }
    }

    /*
     * Di chuyen robot
     */
    public void move(SubCell currentCell, SubCell nextCell) {
        SubCell startCell = new SubCell(0, 0);
        SubCell endCell = new SubCell(0, 0);

        startCell.column = robotCell.column;
        startCell.row = robotCell.row;

        do {
            endCell = robotNextStep(startCell);
            if (endCell == null) {
                break;
            }
            robot.numberstep++;
            robot.battery--;
            moveNextSubCell(startCell, endCell);
            if(keyRedundancy && !isLowBattery()){
                moveSubCellRedundancy(endCell);
            }
            
            System.out.println("->Move from " + startCell.column + "x"
                    + startCell.row + " to " + endCell.column + "x"
                    + endCell.row);
            startCell.column = endCell.column;
            startCell.row = endCell.row;
            robotCell.column = endCell.column;
            robotCell.row = endCell.row;
        } while (!isInBigCell(endCell, nextCell));
    }

    /*
     * Kiem tra xem checkCell co nam trong BigCell dai dien boi bigCell
     */
    public boolean isInBigCell(SubCell checkCell, SubCell bigCell) {
        try {
            return (checkCell.column / 2 == bigCell.column / 2)
                    && (checkCell.row / 2 == bigCell.row / 2);
        } catch (Exception e) {
            moveNextSubCell(checkCell, new SubCell(0, 0));
            robot.numberstep = 0;
            robot.battery = 1000;
            JOptionPane.showMessageDialog(null, "I feel better, I'll continue cleanup", "Full battery!!!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/hecstt2/image/full_battery.png"));
            resetAllBlock();
            onlineSTC(null, new SubCell(1, 1));
            return true;
        }
    }

    /*
     * Trả về danh sách hàng xóm của một Cell dựa trên vị trí Cell cha.
     */
    public ArrayList<SubCell> neighbors(SubCell parentCell, SubCell currentCell) {
        ArrayList<SubCell> neighbors = new ArrayList<>();
        int distanceCol, distanceRow;
        if (parentCell == null) {
            matrix[currentCell.column][currentCell.row].added = true;
            matrix[currentCell.column - 1][currentCell.row].added = true;
            matrix[currentCell.column][currentCell.row - 1].added = true;
            matrix[currentCell.column - 1][currentCell.row - 1].added = true;
            neighbors.add(matrix[currentCell.column][currentCell.row + 2]);
            neighbors.add(matrix[currentCell.column + 2][currentCell.row]);
        } else {
            distanceRow = distance(parentCell, currentCell)[0];
            distanceCol = distance(parentCell, currentCell)[1];
            SubCell n1 = null, n2 = null, n3 = null, n4 = null;
            if (currentCell.row >= 2) {
                n1 = matrix[currentCell.column][currentCell.row - 2];
            }
            if (currentCell.column >= 2) {
                n2 = matrix[currentCell.column - 2][currentCell.row];
            }
            if (currentCell.row < (mapconfig.numberrows - 2)) {
                n3 = matrix[currentCell.column][currentCell.row + 2];
            }
            if (currentCell.column < (mapconfig.numbercolumns - 2)) {
                n4 = matrix[currentCell.column + 2][currentCell.row];
            }

            if (distanceRow == -2 && distanceCol == 0) {
                if (n2 != null) {
                    neighbors.add(n2);
                }
                if (n3 != null) {
                    neighbors.add(n3);
                }
                if (n4 != null) {
                    neighbors.add(n4);
                }
            } else if (distanceRow == 0 && distanceCol == -2) {
                if (n3 != null) {
                    neighbors.add(n3);
                }
                if (n4 != null) {
                    neighbors.add(n4);
                }
                if (n1 != null) {
                    neighbors.add(n1);
                }
            } else if (distanceRow == 2 && distanceCol == 0) {
                if (n4 != null) {
                    neighbors.add(n4);
                }
                if (n1 != null) {
                    neighbors.add(n1);
                }
                if (n2 != null) {
                    neighbors.add(n2);
                }

            } else if (distanceRow == 0 && distanceCol == 2) {
                if (n1 != null) {
                    neighbors.add(n1);
                }
                if (n2 != null) {
                    neighbors.add(n2);
                }
                if (n3 != null) {
                    neighbors.add(n3);
                }
            } else {
                System.out.println("Loi khi add neighbors");
            }
        }

        return neighbors;
    }

    /*
     * Kiem tra da den Big Cell Start chua?
     */
    public boolean isStart(SubCell currentCell) {
        return ((currentCell.column == 0 && currentCell.row == 0)
                || (currentCell.column == 0 && currentCell.row == 1) || (currentCell.column == 1 && currentCell.row == 0));
    }

    /*
     * Xay dung cay khung Edge giua 2 Cell co 2 Subcell dai dien start,end
     */
    public void constructST(SubCell start, SubCell end) {
        ArrayList<SubCell> neighbors = new ArrayList<>();
        SubCell n1 = null, n2 = null, n3 = null, n4 = null;
        if (start.row >= 2) {
            n1 = matrix[start.column][start.row - 2];
        }
        if (start.column >= 2) {
            n2 = matrix[start.column - 2][start.row];
        }
        if (start.row < (mapconfig.numberrows - 2)) {
            n3 = matrix[start.column][start.row + 2];
        }
        if (start.column < (mapconfig.numbercolumns - 2)) {
            n4 = matrix[start.column + 2][start.row];
        }
        neighbors.add(n1);
        neighbors.add(n2);
        neighbors.add(n3);
        neighbors.add(n4);

        if (!neighbors.isEmpty()) {
            if (checkNotOver(end.column, end.row)
                    && !(matrix[end.column][end.row].added)
                    && matrix[end.column][end.row].valuebigcell) {
                matrix[end.column][end.row].added = true;
                matrix[end.column - 1][end.row].added = true;
                matrix[end.column][end.row - 1].added = true;
                matrix[end.column - 1][end.row - 1].added = true;
                this.listSTC.add(new Edge(start, end));

                if (neighbors.get(0) != null) {
                    if (end.column == neighbors.get(0).column
                            && end.row == neighbors.get(0).row) {
                        matrix[start.column][start.row - 1].left = false;
                        matrix[start.column][start.row - 2].left = false;
                        matrix[start.column - 1][start.row - 1].right = false;
                        matrix[start.column - 1][start.row - 2].right = false;
                    }
                }
                if (neighbors.get(1) != null) {
                    if (end.column == neighbors.get(1).column
                            && end.row == neighbors.get(1).row) {
                        matrix[start.column - 1][start.row].top = false;
                        matrix[start.column - 2][start.row].top = false;
                        matrix[start.column - 1][start.row - 1].down = false;
                        matrix[start.column - 2][start.row - 1].down = false;
                    }
                }
                if (neighbors.get(2) != null) {
                    if (end.column == neighbors.get(2).column
                            && end.row == neighbors.get(2).row) {
                        matrix[start.column][start.row].left = false;
                        matrix[start.column - 1][start.row].right = false;
                        matrix[start.column][start.row + 1].left = false;
                        matrix[start.column - 1][start.row + 1].right = false;
                    }
                }
                if (neighbors.get(3) != null) {
                    if (end.column == neighbors.get(3).column
                            && end.row == neighbors.get(3).row) {
                        matrix[start.column][start.row].top = false;
                        matrix[start.column][start.row - 1].down = false;
                        matrix[start.column + 1][start.row - 1].down = false;
                        matrix[start.column + 1][start.row].top = false;
                    }
                }
            }
        }
    }

    public int[] distance(SubCell currentCell, SubCell nextCell) {
        int distance[] = new int[2];
        distance[0] = currentCell.row - nextCell.row;
        distance[1] = currentCell.column - nextCell.column;
        return distance;
    }
    
    public boolean checkInSTC(SubCell next){
        for (Edge tmp : listSTC) {
            SubCell end = tmp.end;
            if(end.column == next.column && end.row == next.row){
                return true;
            }
        }
        return false;
    }
}
