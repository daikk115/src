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

11/12/2015: Lâu lâu vấn các 3 vật gặp nhau, là bị lỗi đứng lại cả 3. 
12/12/2015 00:41 : Cập nhật thời lượng pin cho robot trong thuật toán Online, với pin giảm dần theo từng bước đi còn số bước đi tăng dần, dựa vào đó để cho robot quay về theo con đường cũ.

p/s:
- ý tưởng nếu làm cho Offline, thì dùng thuật toán BFS có thể tìm đường về tốt nhất, sau đó tùy vào lượng pin mà quay về, chứ k phụ thuộc vào 50% lượng pin như các nhóm đã làm. 
- Chắc phải xử lý phần giao diện cho Ok tí đã, sau đó làm lại bài tập cá nhân ok, còn kịp thì làm cái OFFLINE BATTERY
