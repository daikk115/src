package hecstt2.algorithm;

import hecstt2.gui.Edge;
import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OnLine extends Algorithm {
	int numberCols = 0;
	int numberRows = 0;

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
		System.out.println("Move from " + currentCell.column + "x"
				+ currentCell.row + " to " + nextCell.column + "x"
				+ nextCell.row);
		if (nextCell.column == currentCell.column) {
			if (nextCell.row > currentCell.row) {
				for (int i = robot.y; i <= (nextCell.row + 1) * mapconfig.cell; i += 2) {
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
				for (int i = robot.y; i >= (nextCell.row - 1) * mapconfig.cell; i -= 2) {
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
						+ parentCell.row + " - Neighbors of "
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
	 * Kiem tra da den Big Cell Start chua?
	 */
	public boolean isStart(SubCell currentCell) {
		return ((currentCell.column == 1 && currentCell.row == 1)
				|| (currentCell.column == 0 && currentCell.row == 0)
				|| (currentCell.column == 0 && currentCell.row == 1) || (currentCell.row == 1 && currentCell.column == 0));
	}

	public void constructST(SubCell start, SubCell end) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		SubCell n1 = null, n2 = null, n3 = null, n4 = null;
		if (start.row >= 2)
			n1 = matrix[start.column][start.row - 2];
		if (start.column >= 2)
			n2 = matrix[start.column - 2][start.row];
		if (start.row < (mapconfig.numberrows - 2))
			n3 = matrix[start.column][start.row + 2];
		if (start.column < (mapconfig.numbercolumns - 2))
			n4 = matrix[start.column + 2][start.row];
		neighbors.add(n1);
		neighbors.add(n2);
		neighbors.add(n3);
		neighbors.add(n4);

		if (!neighbors.isEmpty()) {
			if (checkNotOver(end.column, end.row)
					&& !matrix[end.column][end.row].added
					&& matrix[end.column][end.row].valuebigcell) {
				matrix[end.column][end.row].added = true;
				// matrix[end.column - 1][end.row].added = true;
				// matrix[end.column][end.row - 1].added = true;
				// matrix[end.column - 1][end.row - 1].added = true;

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

	public void checkMove(SubCell currentCell, SubCell nextCell) {
		int distanceRow = distance(currentCell, nextCell)[0];
		int distanceCol = distance(currentCell, nextCell)[1];
		// xet dieu kien chua du, trong th moveback k dung
		if ((distanceCol == -2) || (distanceRow == -2)) {
			move(matrix[currentCell.column][currentCell.row - 1],
					matrix[nextCell.column][nextCell.row - 1]);
		}

		if ((distanceCol == 2) || (distanceRow == 2)) {
			move(matrix[currentCell.column - 1][currentCell.row],
					matrix[nextCell.column - 1][nextCell.row]);
		}
	}

	/*
	 * Online STC
	 */
	public void onlineSTC(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = neighbors(parentCell, currentCell);
		checkAllBlock(numberCols, numberRows);
		for (int i = 0; i < neighbors.size(); i++) {
			countNumber(currentCell, neighbors.get(i));
			if (!matrix[neighbors.get(i).column][neighbors.get(i).row].valuebigcell)
				continue;
			constructST(currentCell, neighbors.get(i));
			this.frame.repaint();
			System.out.println("Move forward");
			checkMove(currentCell, neighbors.get(i));
			onlineSTC(currentCell, neighbors.get(i));
		}

		if (!isStart(currentCell)) {
			System.out.println("Move back");
			checkMove(currentCell, parentCell);
		}

		if (!matrix[parentCell.column][parentCell.row].valuebigcell) {
			onlineSTC(currentCell, parentCell);
		}
	}
}
