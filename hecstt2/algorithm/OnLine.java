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
		do {
			onlineSTC(null, new SubCell(0, 0));
		} while (numberCols > mapconfig.numbercolumns
				|| numberRows > mapconfig.numberrows);
	}

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
			currentCell.column = nextCell.column;
			currentCell.row = nextCell.row;
		}
	}

	public ArrayList<SubCell> neighbors(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		int distanceCol, distanceRow;
		if (parentCell == null) {
			neighbors.add(matrix[currentCell.column][currentCell.row + 2]);
			neighbors.add(matrix[currentCell.column + 2][currentCell.row]);
		} else {
			distanceCol = parentCell.column - currentCell.column;
			distanceRow = parentCell.row - currentCell.row;
			SubCell n1 = null, n2 = null, n3 = null, n4 = null;
			if (currentCell.row >= 2)
				n1 = matrix[currentCell.column][currentCell.row - 2];
			if (currentCell.column >= 2)
				n2 = matrix[currentCell.column - 2][currentCell.row];
			if (currentCell.row < (mapconfig.numberrows - 2))
				n3 = matrix[currentCell.column][currentCell.row + 2];
			if (currentCell.column < (mapconfig.numbercolumns - 2))
				n4 = matrix[currentCell.column + 2][currentCell.row];

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
				if (n1 != null) {
					neighbors.add(n1);
				}
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
		return neighbors;
	}

	public void countNumber(SubCell currentCell, SubCell nextCell) {
		try {
			int distanceCol, distanceRow;
			distanceCol = currentCell.column - nextCell.column;
			distanceRow = currentCell.row - nextCell.row;
			if (Math.abs(distanceCol) > 0
					&& numberCols <= mapconfig.numbercolumns) {
				numberCols += Math.abs(distanceCol);
			}

			if (Math.abs(distanceRow) > 0 && numberRows <= mapconfig.numberrows) {
				numberRows += Math.abs(distanceRow);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	public void onlineSTC(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = neighbors(parentCell, currentCell);
		checkAllBlock(numberCols, numberRows);
		for (int i = 0; i < neighbors.size(); i++) {
			System.out.println("Toa do Neighbor thu " + i + ":"
					+ neighbors.get(i).column + "x" + neighbors.get(i).row);
			countNumber(currentCell, neighbors.get(i));
			if (!matrix[neighbors.get(i).column][neighbors.get(i).row].valuebigcell) {
				continue;
			}
			constructST(currentCell, neighbors.get(i));
			this.frame.repaint();
			move(currentCell, neighbors.get(i));
			onlineSTC(currentCell, neighbors.get(i));
		}

		if (currentCell.column != 0 && currentCell.row != 0) {
			move(currentCell, parentCell);
		}
	}
}
