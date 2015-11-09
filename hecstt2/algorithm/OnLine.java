package hecstt2.algorithm;

import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnLine extends Algorithm {
	int numberCols = 1;
	int numberRows = 1;

	public OnLine(MyGraphics frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		onlineSTC(null, new SubCell(1, 1));
	}

	/*
	 * Di chuyen giua 2 subcell
	 */
	public void move(SubCell currentCell, SubCell nextCell) {
		if (nextCell.column == currentCell.column) {
			if (nextCell.row > currentCell.row) {
				for (int i = robot.y; i < nextCell.row * mapconfig.cell; i += 2) {
					try {
						robot.y = i;
						Thread.sleep(20);
						this.frame.repaint(robot.x, robot.y, mapconfig.cell,
								mapconfig.cell);
					} catch (InterruptedException ex) {
						Logger.getLogger(MyGraphics.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			} else {
				for (int i = robot.y; i >= nextCell.row * mapconfig.cell; i -= 2) {
					try {
						robot.y = i;
						Thread.sleep(20);
						frame.repaint(robot.x, robot.y, mapconfig.cell,
								mapconfig.cell);
					} catch (InterruptedException ex) {
						Logger.getLogger(MyGraphics.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
		} else {
			if (nextCell.column > currentCell.column) {
				for (int i = robot.x; i <= nextCell.column * mapconfig.cell; i += 2) {
					try {
						robot.x = i;
						Thread.sleep(10);
						frame.repaint(robot.x, robot.y, mapconfig.cell,
								mapconfig.cell);
					} catch (InterruptedException ex) {
						Logger.getLogger(MyGraphics.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			} else {
				for (int i = robot.x; i >= nextCell.column * mapconfig.cell; i -= 2) {
					try {
						robot.x = i;
						Thread.sleep(10);
						frame.repaint(robot.x, robot.y, mapconfig.cell,
								mapconfig.cell);
					} catch (InterruptedException ex) {
						Logger.getLogger(MyGraphics.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(OffLine.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			frame.repaint(robot.x, robot.y, mapconfig.cell, mapconfig.cell);
			// currentCell.column = nextCell.column;
			// currentCell.row = nextCell.row;
		}
	}

	/*
	 * Trả về danh sách hàng xóm của một Cell dựa trên vị trí Cell cha.
	 */
	public ArrayList<SubCell> neighbors(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		int distanceCol, distanceRow;
		if (parentCell == null) {
			neighbors.add(matrix[currentCell.column][currentCell.row + 2]);
			neighbors.add(matrix[currentCell.column + 2][currentCell.row]);
		} else {
			distanceCol = parentCell.column - currentCell.column;
			distanceRow = parentCell.row - currentCell.row;
			System.out.println("Distance : " + parentCell.column + " "
					+ currentCell.column);
			System.out.println("Distance : " + parentCell.row + " "
					+ currentCell.row);
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
				if (n2 != null)
					neighbors.add(n2);
				if (n3 != null)
					neighbors.add(n3);
				if (n4 != null)
					neighbors.add(n4);
			} else if (distanceRow == 0 && distanceCol == -2) {
				if (n3 != null)
					neighbors.add(n3);
				if (n4 != null)
					neighbors.add(n4);
				if (n1 != null)
					neighbors.add(n1);
			} else if (distanceRow == 2 && distanceCol == 0) {
				if (n4 != null)
					neighbors.add(n4);
				if (n1 != null)
					neighbors.add(n1);
				if (n2 != null)
					neighbors.add(n2);

			} else if (distanceRow == 0 && distanceCol == 2) {
				if (n1 != null)
					neighbors.add(n1);
				if (n2 != null)
					neighbors.add(n2);
				if (n3 != null)
					neighbors.add(n3);
			} else {
				System.out.println("Loi khi add neighbors");
			}
		}

		for (int i = 0; i < neighbors.size(); i++) {
			if (parentCell != null)
				System.out.println("Parent:" + parentCell.column + "x"
						+ parentCell.row + " Neighbors of "
						+ currentCell.column + "x" + currentCell.row + ":"
						+ neighbors.get(i).column + "x" + neighbors.get(i).row);
		}

		return neighbors;
	}

	/*
	 * Phuong thuc tinh so hang + so cot da duoc duyet! Chua tham dinh tinh chac
	 * chan
	 */
	public void countNumber(SubCell currentCell, SubCell nextCell) {
		if (Math.abs(nextCell.column) > numberCols)
			numberCols = nextCell.column;
		if (Math.abs(nextCell.row) > numberRows)
			numberRows = nextCell.row;
	}

	/*
	 * Online STC
	 */
	public void onlineSTC(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = neighbors(parentCell, currentCell);
		checkAllBlock(numberCols, numberRows);
		for (int i = 0; i < neighbors.size(); i++) {
			checkAllBlock(numberCols, numberRows);
			countNumber(currentCell, neighbors.get(i));
			if (!matrix[neighbors.get(i).column][neighbors.get(i).row].valuebigcell) 
				continue;
			constructST(currentCell, neighbors.get(i));
			this.frame.repaint();
			System.out.println("Move forward");
			move(currentCell, neighbors.get(i));
			onlineSTC(currentCell, neighbors.get(i));
		}

		if (currentCell.column != 1 && currentCell.row != 1) {
			System.out.println("Move back");
			move(currentCell, parentCell);
		}

		if (!matrix[parentCell.column][parentCell.row].valuebigcell) {
			onlineSTC(currentCell, parentCell);
		}
	}
}
