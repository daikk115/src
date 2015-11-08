package hecstt2.algorithm;

import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

import java.util.ArrayList;

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

	public ArrayList<SubCell> neighbors(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		int distanceCol, distanceRow;
		distanceCol = parentCell.column - currentCell.column;
		distanceRow = parentCell.row - currentCell.row;
		SubCell n1, n2, n3, n4;
		n1 = matrix[currentCell.column][currentCell.row - 2];
		n2 = matrix[currentCell.column - 2][currentCell.row];
		n3 = matrix[currentCell.column][currentCell.row + 2];
		n4 = matrix[currentCell.column + 2][currentCell.row];
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
		}
		return neighbors;
	}

	public void onlineSTC(SubCell parentCell, SubCell currentCell) {
		ArrayList<SubCell> neighbors = neighbors(parentCell, currentCell);
		for (int i = 0; i < neighbors.size(); i++) {
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
