/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.algorithm;

import hecstt2.gui.Edge;
import hecstt2.gui.MapConfig;
import hecstt2.gui.MyGraphics;
import hecstt2.gui.MyRobot;
import hecstt2.gui.SubCell;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author daidv
 */
public class Algorithm extends Thread {

	public SubCell[][] matrix;
	public MyRobot robot;
	public MapConfig mapconfig;
	public JFrame frame;
	public ArrayList<Edge> listSTC;

	/**
	 * truyền matrix để thuật toán hiểu bản đồ. truyền map để hàm vẽ có thể vẽ
	 * lên đó. truyền img để lấy kích thước panel từ bên kia luôn, k phải truyền
	 * mapconfig bên này nữa
	 *
	 * @param frame
	 */
	public Algorithm(MyGraphics frame) {
		this.matrix = frame.matrix;
		this.robot = frame.robot;
		this.frame = frame;
		this.mapconfig = frame.mapconfig;
		this.listSTC = frame.listSTC;
	}

	@Override
	public void run() {

	}

	public void checkAllBlock(int numberCols, int numberRows) {
		for (int i = 0; i < numberCols; i += 2) {
			for (int j = 0; j < numberRows; j += 2) {
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

	/**
	 * * Thuật toán STC sử dụng BFS để tìm kiếm cây khung.
	 */
	public void searchSTC() {
		SubCell currentcell = matrix[1][1]; // bắt đầu từ vị trí 1,1 , có thể
											// truyền đầu vào sau này nếu như
											// đặt robot
		ArrayList<SubCell> stack = new ArrayList<>();
		SubCell tmp = new SubCell(0, 0);
		stack.add(currentcell);
		while (!stack.isEmpty()) {

			currentcell = stack.get(stack.size() - 1);
			stack.remove(stack.size() - 1);
			// kiểm tra đi sang phải
			tmp.column = currentcell.column + 2;
			tmp.row = currentcell.row;
			if (checkNotOver(tmp.column, tmp.row)) { // kiểm tra tmp có vượt quá
														// khỏi bản đồ không
				if (matrix[tmp.column][tmp.row].valuebigcell) { // xem khối đó
																// có thuộc khối
																// lớn có thể đi
																// được k?
					if (!matrix[tmp.column][tmp.row].added) { // kiểm tra
																// subcell đã
																// thuộc hàng
																// đợi hay chưa?
						stack.add(matrix[tmp.column][tmp.row]); // thêm 1
																// Subcell - đại
																// diện cho cả
																// CELL lớn
						matrix[tmp.column][tmp.row].added = true; // gán cờ -
																	// subcell
																	// đã thuộc
																	// stack
						this.listSTC.add(new Edge(currentcell, tmp)); // thêm 1
																		// cạnh
																		// vào
																		// danh
																		// sách
																		// cạnh

						matrix[tmp.column - 1][tmp.row].added = true;
						matrix[tmp.column][tmp.row - 1].added = true;
						matrix[tmp.column - 1][tmp.row - 1].added = true;
						// gán các giá trị để hiểu cây khung
						matrix[currentcell.column][currentcell.row].top = false;
						matrix[currentcell.column][currentcell.row - 1].down = false;
						matrix[currentcell.column + 1][currentcell.row - 1].down = false;
						matrix[currentcell.column + 1][currentcell.row].top = false;
					}
				}
			}
			tmp.column = currentcell.column;
			tmp.row = currentcell.row + 2;
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& !matrix[tmp.column][tmp.row].added) {
				stack.add(matrix[tmp.column][tmp.row]);
				matrix[tmp.column][tmp.row].added = true;
				this.listSTC.add(new Edge(currentcell, tmp));

				matrix[tmp.column - 1][tmp.row].added = true;
				matrix[tmp.column][tmp.row - 1].added = true;
				matrix[tmp.column - 1][tmp.row - 1].added = true;
				matrix[currentcell.column][currentcell.row].left = false;
				matrix[currentcell.column - 1][currentcell.row].right = false;
				matrix[currentcell.column][currentcell.row + 1].left = false;
				matrix[currentcell.column - 1][currentcell.row + 1].right = false;
			}

			tmp.column = currentcell.column - 2;
			tmp.row = currentcell.row;
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& !matrix[tmp.column][tmp.row].added) {
				stack.add(matrix[tmp.column][tmp.row]);
				matrix[tmp.column][tmp.row].added = true;
				this.listSTC.add(new Edge(currentcell, tmp));

				matrix[tmp.column - 1][tmp.row].added = true;
				matrix[tmp.column][tmp.row - 1].added = true;
				matrix[tmp.column - 1][tmp.row - 1].added = true;
				matrix[currentcell.column - 1][currentcell.row].top = false;
				matrix[currentcell.column - 2][currentcell.row].top = false;
				matrix[currentcell.column - 1][currentcell.row - 1].down = false;
				matrix[currentcell.column - 2][currentcell.row - 1].down = false;
			}

			tmp.column = currentcell.column;
			tmp.row = currentcell.row - 2;
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& !matrix[tmp.column][tmp.row].added) {
				stack.add(matrix[tmp.column][tmp.row]);
				matrix[tmp.column][tmp.row].added = true;
				this.listSTC.add(new Edge(currentcell, tmp));

				matrix[tmp.column - 1][tmp.row].added = true;
				matrix[tmp.column][tmp.row - 1].added = true;
				matrix[tmp.column - 1][tmp.row - 1].added = true;
				matrix[currentcell.column][currentcell.row - 1].left = false;
				matrix[currentcell.column][currentcell.row - 2].left = false;
				matrix[currentcell.column - 1][currentcell.row - 1].right = false;
				matrix[currentcell.column - 1][currentcell.row - 2].right = false;
			}
		}
	}

	/**
	 * truyền vào các biến x, y ~ column và row nhằm tái sử dụng cho cả SUBCELL
	 * và ROBOT
	 */
	public boolean checkNotOver(int x, int y) {
		return x >= 0 && y >= 0 && x < mapconfig.numbercolumns
				&& y < mapconfig.numberrows;
	}

	/*
	 * Phuong thuc de cau truc ST(dua vao code trong searchSTC cua DaiDV, co the
	 * dung chung giua OFFLINE & ONLINE
	 */
	public void constructST(SubCell start, SubCell end) {
		ArrayList<SubCell> neighbors = new ArrayList<>();
		SubCell n1, n2, n3, n4;
		n1 = matrix[start.column][start.row - 2];
		n2 = matrix[start.column - 2][start.row];
		n3 = matrix[start.column][start.row + 2];
		n4 = matrix[start.column + 2][start.row];
		neighbors.add(n1);
		neighbors.add(n2);
		neighbors.add(n3);
		neighbors.add(n4);

		if (checkNotOver(end.column, end.row)) {
			matrix[end.column][end.row].added = true;
			matrix[end.column - 1][end.row].added = true;
			matrix[end.column][end.row - 1].added = true;
			matrix[end.column - 1][end.row - 1].added = true;

			this.listSTC.add(new Edge(start, end));

			if (end.column == neighbors.get(0).column
					&& end.row == neighbors.get(0).row) {

				matrix[start.column][start.row - 1].left = false;
				matrix[start.column][start.row - 2].left = false;
				matrix[start.column - 1][start.row - 1].right = false;
				matrix[start.column - 1][start.row - 2].right = false;
			} else if (end.column == neighbors.get(1).column
					&& end.row == neighbors.get(1).row) {
				matrix[start.column - 1][start.row].top = false;
				matrix[start.column - 2][start.row].top = false;
				matrix[start.column - 1][start.row - 1].down = false;
				matrix[start.column - 2][start.row - 1].down = false;
			} else if (end.column == neighbors.get(2).column
					&& end.row == neighbors.get(2).row) {
				matrix[start.column][start.row].left = false;
				matrix[start.column - 1][start.row].right = false;
				matrix[start.column][start.row + 1].left = false;
				matrix[start.column - 1][start.row + 1].right = false;
			} else if (end.column == neighbors.get(3).column
					&& end.row == neighbors.get(3).row) {
				matrix[start.column][start.row].top = false;
				matrix[start.column][start.row - 1].down = false;
				matrix[start.column + 1][start.row - 1].down = false;
				matrix[start.column + 1][start.row].top = false;
			}
		}
	}
}
