# src
Code java hệ cơ sở tri thức: bài toán tìm đường đi cho robot  lau nhà

**Bài tập lớn môn Hệ cơ sở tri thức.**

Các dữ liệu được truyền vào đối tượng thuật toán và thực thi.
Sau mỗi vòng lặp robot di chuyển thì gọi frame.repaint(...) để cập nhật giao diện

**Phân công:**

Đại : code thuật toán offline 

Kiên: code thuật toán online


** Ghi chú **
T thấy hoạt động của nó là các hàm draw thực thi các công việc, xong khi gọi repaint() thì nó mới cập nhật lại dữ liệu, thành ra, mình thay đổi vị trí robot chỉ cần vẽ lại chỗ đó + xê dịch 1 chút là ok.

Tương tự khi click chuột để tạo các block, mình sẽ cập nhật nó vào value của 1 SubCell cụ thể và gọi repaint() mỗi cho đó là xong. Tất nhiên, trong hàm repaint() đã có hàm vẽ tất cả block dựa vào matrix các SubCell.

Lâu lâu vấn các 3 vật gặp nhau, là bị lỗi đứng lại cả 3. 
