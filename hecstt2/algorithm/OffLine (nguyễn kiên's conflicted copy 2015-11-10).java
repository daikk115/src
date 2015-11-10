/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hecstt2.algorithm;

import hecstt2.gui.MyGraphics;
import hecstt2.gui.SubCell;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daidv
 */
public class OffLine extends Algorithm {

	/**
	 * kế thưa từ lớp thuật toán với các hàm vẽ định nghĩa bên kia, bên này chỉ
	 * định nghĩa thuật toán chạy ntn
	 *
	 * @param frame
	 */
	public OffLine(MyGraphics frame) {
		super(frame);
	}

	@Override
	public void run() {

		SubCell start = new SubCell(0, 0); // lưu lại để so sánh thôi. tạo đọ
											// Cell ban đầu robot đang đứng
		SubCell current = new SubCell(0, 0);
		SubCell nextcell = new SubCell(0, 0);

		// lúc này, các Subcell thuộc cây khung -> đều đã bị gán added = true.
		// Dựa vào điểm này, chúng ta sử dụng nó vơi chức năng ngược lại để di
		// chuyển theo cây khung.
		do {

			// System.out.println(current.column + " " + current.row);
			// for (int i = 0; i < mapconfig.numberrows; i++) {
			// for (int j = 0; j < mapconfig.numbercolumns; j++) {
			// System.out.print(matrix[j][i].added + "\t");
			// }
			// System.out.println("");
			// }
			// System.out.println("");
			nextcell = robotNextStep(current); // sau khi lấy vị trí tiếp theo,
												// cho con robot chạy tới vị trí
												// đó từ từ
			if (nextcell == null)
				break;
			if (nextcell.column == current.column) {
				if (nextcell.row > current.row) {
					for (int i = robot.y; i <= nextcell.row * mapconfig.cell; i += 2) {
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
				} else {
					for (int i = robot.y; i >= nextcell.row * mapconfig.cell; i -= 2) {
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
				if (nextcell.column > current.column) {
					for (int i = robot.x; i <= nextcell.column * mapconfig.cell; i += 2) {
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
					for (int i = robot.x; i >= nextcell.column * mapconfig.cell; i -= 2) {
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
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(OffLine.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			frame.repaint(robot.x, robot.y, mapconfig.cell, mapconfig.cell);
			current.column = nextcell.column;
			current.row = nextcell.row;
		} while (!(start.column == current.column && start.row == current.row));
	}

	public SubCell robotNextStep(SubCell cellrobot) {

		// xét 4 hướng thôi
		SubCell tmp = new SubCell(0, 0);
		ArrayList<SubCell> select = new ArrayList<>();

		matrix[cellrobot.column][cellrobot.row].added = false;

		tmp.column = cellrobot.column + 1;
		tmp.row = cellrobot.row;
		if (matrix[cellrobot.column][cellrobot.row].right) {
			if (checkNotOver(tmp.column, tmp.row)) { // kiểm tra tmp có vượt quá
														// khỏi bản đồ không
				if (matrix[tmp.column][tmp.row].valuebigcell) { // xem khối đó
																// có thuộc khối
																// lớn có thể đi
																// được k?
					if (matrix[tmp.column][tmp.row].added) { // kiểm tra subcell
																// đã thuộc hàng
																// đợi hay chưa?
						select.add(matrix[tmp.column][tmp.row]);
					}
				}
			}
		}
		tmp.column = cellrobot.column;
		tmp.row = cellrobot.row + 1;
		if (matrix[cellrobot.column][cellrobot.row].down) {
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& matrix[tmp.column][tmp.row].added) {
				select.add(matrix[tmp.column][tmp.row]);
			}
		}

		tmp.column = cellrobot.column - 1;
		tmp.row = cellrobot.row;
		if (matrix[cellrobot.column][cellrobot.row].left) {
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& matrix[tmp.column][tmp.row].added) {
				select.add(matrix[tmp.column][tmp.row]);
			}
		}

		tmp.column = cellrobot.column;
		tmp.row = cellrobot.row - 1;
		if (matrix[cellrobot.column][cellrobot.row].top) {
			if (checkNotOver(tmp.column, tmp.row)
					&& matrix[tmp.column][tmp.row].valuebigcell
					&& matrix[tmp.column][tmp.row].added) {
				select.add(matrix[tmp.column][tmp.row]);
			}
		}
		if (!select.isEmpty()) {
			for (SubCell abc : select) {
				if (checkDirection(abc, cellrobot)) {
					// uu tien cung canh
					return abc;
				} else {
					if ((abc.column / 2 == cellrobot.column / 2)
							&& (abc.row / 2 == cellrobot.row / 2)) {
						// uu tien cung CELL
						return abc;
					}
				}
			}
		}
		if (select.isEmpty()) {
			return null;
		} else {
			return select.get(0);
		}
	}

	public boolean checkDirection(SubCell a, SubCell b) {
		a = matrix[a.column][a.row];
		b = matrix[b.column][b.row];
		return (!a.top && !b.top) || (!a.down && !b.down)
				|| (!a.left && !b.left) || (!a.right && !b.right);
	}
}
