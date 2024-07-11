
# Trang web thi trắc nghiệm toán MathQuiz

Chào mừng bạn đến với trang web thi trắc nghiệm toán MathQuiz! Nền tảng này cung cấp các bài kiểm tra toán hấp dẫn và giáo dục dành cho học sinh để nâng cao kỹ năng toán học của họ.

## Tính năng

- **Xác thực người dùng**: Hệ thống đăng nhập và đăng ký bảo mật cho học sinh và giáo viên.
- **Nhiều bài kiểm tra**: Câu hỏi trắc nghiệm bao gồm các chủ đề toán học và mức độ khó khác nhau.
- **Tùy chọn câu hỏi động**: Cập nhật theo thời gian thực cho các tùy chọn câu hỏi đã chọn để phù hợp với nhu cầu học tập cá nhân.
- **Theo dõi kết quả**: Theo dõi điểm số chi tiết và lịch sử cho học sinh để giám sát tiến trình của họ.
- **Thiết kế đáp ứng**: Truy cập trên nhiều thiết bị, đảm bảo trải nghiệm mượt mà trên cả máy tính để bàn và thiết bị di động.

## Bắt đầu

### Yêu cầu

- Java 11 trở lên
- Maven 3.6.0 trở lên

### Cài đặt

1. **Clone kho lưu trữ:**
    ```sh
    git clone https://github.com/NghiaDau/MathQuizJava.git
    cd math-quiz-website
    ```

2. **Cài đặt Backend:**
    - Điều hướng đến thư mục backend:
      ```sh
      cd backend
      ```
    - Cài đặt các phụ thuộc cần thiết:
      ```sh
      mvn clean install
      ```
    - Chạy ứng dụng Spring Boot:
      ```sh
      mvn spring-boot:run
      ```

3. **Cài đặt Frontend:**
    - Điều hướng đến thư mục frontend:
      ```sh
      cd frontend
      ```
    - Cài đặt các phụ thuộc cần thiết:
      ```sh
      npm install
      ```
    - Chạy ứng dụng React:
      ```sh
      npm start
      ```

4. **Truy cập Ứng dụng:**
    - Mở trình duyệt của bạn và điều hướng đến `http://localhost:8080`

## Sử dụng

1. **Đăng ký và Đăng nhập:**
   - Học sinh và giáo viên có thể đăng ký tài khoản hoặc đăng nhập nếu đã có tài khoản.

2. **Thực hiện bài kiểm tra:**
   - Duyệt qua các bài kiểm tra có sẵn và chọn một bài để bắt đầu.
   - Trả lời các câu hỏi trắc nghiệm và nộp bài kiểm tra.

3. **Xem kết quả:**
   - Sau khi hoàn thành bài kiểm tra, xem điểm số và kết quả chi tiết của bạn.
   - Theo dõi tiến trình của bạn theo thời gian trong phần hồ sơ.

## Đóng góp

Chúng tôi hoan nghênh các đóng góp để cải thiện trang web thi trắc nghiệm toán! Để đóng góp, hãy làm theo các bước sau:

1. Fork kho lưu trữ.
2. Tạo một nhánh mới (`git checkout -b feature/your-feature`).
3. Commit các thay đổi của bạn (`git commit -m 'Add some feature'`).
4. Push lên nhánh (`git push origin feature/your-feature`).
5. Mở một pull request.

## Giấy phép

Dự án này được cấp phép theo giấy phép MIT.

## Liên hệ

Nếu bạn có bất kỳ câu hỏi hoặc đề xuất nào, vui lòng liên hệ với chúng tôi tại todreamscompany@gmail.com.

Chúc bạn học tốt!
