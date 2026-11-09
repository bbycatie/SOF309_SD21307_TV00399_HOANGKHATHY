# BÁO CÁO ASSIGNMENT MÔN SOF309: KIỂM THỬ NÂNG CAO CÙNG AI

- **Họ và tên:** Hoàng Kha Thy
- **Môn học:** SOF309 - Kiểm thử nâng cao cùng AI
- **Chức năng kiểm thử:** Đăng nhập hệ thống (SauceDemo Web App)

---

## I. BẢNG KỊCH BẢN KIỂM THỬ (TEST CASES)

| Mã TC | Tên kịch bản | Dữ liệu đầu vào (Username / Password) | Kết quả mong đợi | Loại kiểm thử | Nhóm (Group) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC01** | Đăng nhập thành công | `standard_user` / `secret_sauce` | Chuyển hướng đến trang `inventory.html` | Valid Data | `smoke` |
| **TC02** | Sai mật khẩu | `standard_user` / `wrong_password` | Báo lỗi "Username and password do not match..." | Invalid Data | `regression` |
| **TC03** | Mật khẩu để trống | `standard_user` / `""` | Báo lỗi "Password is required" | Boundary Check | `regression` |
| **TC04** | Tài khoản bị khóa | `locked_out_user` / `secret_sauce` | Báo lỗi "Sorry, this user has been locked out." | Exception | `regression` |

---

## II. KỸ THUẬT KIỂM THỬ ĐÃ ÁP DỤNG TRONG MÃ NGUỒN

1. **DataProvider & Parameterized Test:** Quản lý tập dữ liệu kiểm thử tập trung bằng `@DataProvider`.
2. **Grouping:** Phân chia bài test thành các nhóm (`smoke`, `regression`, `exception`).
3. **Exception Handling:** Sử dụng `expectedExceptions` kiểm tra ngoại lệ Selenium.
4. **Screenshot on Failure:** Tự động chụp ảnh màn hình lưu vào `./screenshots/` khi test case bị lỗi.