
# Movie Streaming Platform – Hệ thống xem phim trực tuyến

## 🧩 Mục tiêu

Xây dựng một hệ thống web xem phim theo kiến trúc microservices, hỗ trợ:

- Người dùng đăng ký, đăng nhập bảo mật bằng JWT
- Duyệt và xem danh sách phim, thể loại
- Xem phim trực tiếp (streaming)
- Đánh giá và lưu lịch sử xem
- Hệ thống mở rộng dễ dàng, dễ bảo trì và theo dõi tình trạng hoạt động

## 🏗️ Tổng quan Kiến trúc

```text
                           +---------------------+
                           |  Spring Cloud Config|
                           |    (config-server)  |
                           +----------+----------+
                                      |
          +---------------------------+------------------------------+
          |                           |                              |
 +--------v----------+   +------------v-----------+   +--------------v-----------+
 | Eureka Discovery  |   | Spring Cloud Gateway   |   | Spring Boot Admin (UI)   |
 |     (Eureka)      |   |     (api-gateway)      |   |      (tuỳ chọn)          |
 +--------+----------+   +------------+-----------+   +-------------------------+
          |                           |
          |               +-----------+----------+
          |               |   Spring Security    |
          |               | (JWT Auth Service)   |
          |               +-----------+----------+
          |                           |
 +--------v----------+  +------------v-----------+  +-----------v----------+
 |  user-service     |  |  catalog-service       |  |  movie-service       |
 | (quản lý người dùng)| | (thể loại)            |  | (thông tin phim)     |
 +-------------------+  +-----------------------+  +----------------------+
                             |                             |
                   +---------v----------+     +-----------v-----------+
                   | review-service     |     | history-service       |
                   | (đánh giá phim)    |     | (lịch sử xem)         |
                   +--------------------+     +-----------------------+
                             |
                      +------v------+
                      | media-stream |
                      | -service     |
                      | (stream video)|
                      +--------------+
```

## 🗂️ Các Service trong hệ thống

| Tên Service           | Mô tả chức năng chính                                      |
|-----------------------|------------------------------------------------------------|
| config-server         | Quản lý cấu hình tập trung từ repo config-repo             |
| eureka-server         | Quản lý đăng ký và khám phá các service                    |
| api-gateway           | Cổng truy cập vào toàn bộ hệ thống, xử lý route, filter, auth |
| auth-service          | Xử lý xác thực, tạo JWT, kiểm tra token                    |
| user-service          | Quản lý người dùng: đăng ký, thông tin cá nhân, vai trò    |
| catalog-service       | Quản lý danh mục phim: thể loại, danh sách phim theo thể loại |
| movie-service         | Thông tin chi tiết phim: tên, mô tả, thời lượng, diễn viên |
| review-service        | Đánh giá phim, rating, comment                             |
| history-service       | Lưu lịch sử xem phim theo người dùng                       |
| media-stream-service  | Stream nội dung video, hỗ trợ Byte Range để stream hiệu quả|

## 🛠️ Công nghệ sử dụng

| Thành phần           | Công nghệ                                         |
|----------------------|---------------------------------------------------|
| Framework Backend    | Spring Boot, Spring Cloud                         |
| Service Discovery    | Spring Cloud Netflix Eureka                       |
| Load Balancing       | Spring Cloud LoadBalancer                         |
| API Gateway          | Spring Cloud Gateway                              |
| Auth                 | Spring Security + JWT                             |
| Config               | Spring Cloud Config Server + Git                  |
| DB chính             | PostgreSQL (cho movie, catalog, user)             |
| DB phụ               | MongoDB (cho review, history)                     |
| Stream Video         | Spring Web + Http Byte Range                      |
| Monitoring           | Spring Boot Actuator, Micrometer, Spring Boot Admin (tùy chọn) |

## 💾 Database cho từng Service

| Service              | Database                                               |
|----------------------|-------------------------------------------------------|
| user-service         | PostgreSQL                                      |
| movie-service        | MongoDB (dữ liệu phim động, dễ mở rộng)               |
| auth-service         | PostgreSQL (tài khoản, refresh token)                |
| history-service      | MongoDB (xem lịch sử, thời gian, đoạn đã xem)        |
| review-service       | MongoDB                                               |
| catalog-service      | PostgreSQL                                           |
| media-stream-service | Không cần DB, chỉ đọc file/video từ thư mục hoặc cloud |

## 🔐 Luồng xử lý (Flow) người dùng

1. Người dùng truy cập qua api-gateway
2. Đăng nhập → auth-service sinh JWT → Trả token
3. Người dùng gửi request có Bearer token đến gateway
4. Gateway kiểm tra token (via auth-service hoặc local filter)
5. Gateway route request đến các service:
    - `/user/**` → user-service
    - `/movies/**` → movie-service
    - `/catalog/**` → catalog-service
    - `/stream/**` → media-stream-service
    - ...
6. Các service lấy cấu hình từ config-server và đăng ký vào eureka
7. Spring Cloud LoadBalancer tự động chọn instance khi gọi nội bộ
8. Actuator cung cấp health-check cho Eureka

## 📂 Cấu trúc thư mục gợi ý

```text
movie-streaming-platform/
│
├── config-server/
├── config-repo/               ← Chứa file YAML cấu hình
│   ├── auth-service.yml
│   ├── movie-service.yml
│   └── ...
├── eureka-server/
├── api-gateway/
├── auth-service/
├── user-service/
├── movie-service/
├── catalog-service/
├── review-service/
├── history-service/
├── media-stream-service/
├── common/                    ← Chứa DTOs, utils dùng chung
└── pom.xml                    ← Nếu là Maven multi-module
```

## 💡 Đề tài mở rộng

- Phân quyền vai trò (admin, user, guest)
- Tìm kiếm nâng cao (search theo tag, rating)
- Gợi ý phim theo lịch sử xem
- Đánh dấu yêu thích, xem sau
- Dùng Elasticsearch cho tìm kiếm