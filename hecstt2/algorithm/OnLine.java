package hecstt2.algorithm;

import java.util.ArrayList;

import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

public class OnLine extends Algorithm {

	public OnLine(MyGraphics frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

	}

	public void move(SubCell currentCell, SubCell nextCell) {

	}

	public ArrayList<SubCell> neighbors(SubCell currentCell, SubCell parentCell) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		int distanceCol, distanceRow;
		distanceCol = parentCell.column - currentCell.column;
		distanceRow = parentCell.row - currentCell.row;
		SubCell n1, n2, n3, n4;
		// n1 = matrix[currentCell.column][currentCell.row - 1];
		n1 = new SubCell(currentCell.column, currentCell.row - 1);
		n2 = matrix[currentCell.column - 1][currentCell.row];
		n3 = matrix[currentCell.column][currentCell.row + 1];
		n4 = matrix[currentCell.column + 1][currentCell.row];
		if (distanceRow == -2 && distanceCol == 0) {
			neighbors.add(n2);
			neighbors.add(n3);
			neighbors.add(n4);
		} else if (distanceRow == 0 && distanceCol == -2) {
			neighbors.add(n3);
			neighbors.add(n4);
			neighbors.add(n1);
		} else if (distanceRow == 2 && distanceCol == 0) {
			neighbors.add(n4);
			neighbors.add(n1);
			neighbors.add(n2);
		} else if (distanceRow == 0 && distanceCol == 2) {
			neighbors.add(n1);
			neighbors.add(n2);
			neighbors.add(n3);
		} else {
			System.out.println("Invalid parent cell!");
		}
		return neighbors;
	}

	public void onlineAlgorithm() {

	}
}
