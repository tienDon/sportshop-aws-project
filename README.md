# 🏃 SportShop - E-Commerce Platform

Nền tảng thương mại điện tử chuyên về sản phẩm thể thao với đầy đủ tính năng quản lý sản phẩm, đơn hàng, chat real-time và tích hợp AWS.

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
- [Cấu hình môi trường](#cấu-hình-môi-trường)
- [API Documentation](#api-documentation)
- [Cấu trúc Database](#cấu-trúc-database)
- [Deployment](#deployment)
- [Đóng góp](#đóng-góp)

## 🎯 Tổng quan

SportShop là một nền tảng thương mại điện tử hoàn chỉnh được xây dựng với kiến trúc **Full-Stack**:
- **Backend**: Spring Boot REST API với JWT Authentication
- **Frontend**: React + TypeScript với Vite
- **Database**: MySQL
- **Cloud Services**: AWS (S3, SES, SNS, Cognito, Parameter Store)
- **Real-time**: WebSocket cho tính năng chat

## ✨ Tính năng

### 👤 Người dùng
- ✅ Đăng ký/Đăng nhập bằng OTP (Email/SMS)
- ✅ Quản lý thông tin cá nhân và địa chỉ
- ✅ Duyệt sản phẩm theo danh mục, thương hiệu, môn thể thao
- ✅ Tìm kiếm và lọc sản phẩm
- ✅ Xem chi tiết sản phẩm với nhiều biến thể (màu sắc, kích thước)
- ✅ Quản lý giỏ hàng
- ✅ Đặt hàng và theo dõi đơn hàng
- ✅ Chat real-time với admin

### 👨‍💼 Admin
- ✅ Dashboard quản lý tổng quan
- ✅ Quản lý sản phẩm (CRUD, upload ảnh, variants)
- ✅ Quản lý danh mục (3 cấp độ)
- ✅ Quản lý thương hiệu, môn thể thao, đối tượng
- ✅ Quản lý thuộc tính sản phẩm (attributes, colors, sizes)
- ✅ Quản lý đơn hàng
- ✅ Quản lý người dùng
- ✅ Chat hỗ trợ khách hàng real-time
- ✅ Upload file chat (hỗ trợ S3 hoặc local storage)

## 🛠 Công nghệ sử dụng

### Backend
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security + JWT
- **WebSocket**: Spring WebSocket (STOMP)
- **Mapping**: MapStruct
- **Build Tool**: Maven
- **AWS SDK**: 
  - S3 (File Storage)
  - SES (Email Service)
  - SNS (SMS Service)
  - Cognito (User Management)
  - Parameter Store (Configuration)

### Frontend
- **Framework**: React 19.2.0
- **Language**: TypeScript 5.9.3
- **Build Tool**: Vite 7.2.4
- **UI Framework**: 
  - Tailwind CSS 4.1.17
  - shadcn/ui (Radix UI components)
- **State Management**: 
  - Zustand (Global state)
  - React Query (Server state)
- **Routing**: React Router 7
- **Forms**: React Hook Form + Zod
- **HTTP Client**: Axios
- **WebSocket**: SockJS + STOMP.js
- **Notifications**: Sonner

## 📁 Cấu trúc dự án

```
sport-shop/
├── sportshope-commerceplatform-be/     # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ojtteamaws/sportshopecommerceplatformbe/
│   │   │   │   ├── config/             # Cấu hình (Security, AWS, WebSocket)
│   │   │   │   ├── controller/         # REST Controllers (17 controllers)
│   │   │   │   ├── entity/             # JPA Entities (26 entities)
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   │   ├── Request/        # Request DTOs
│   │   │   │   │   └── Response/       # Response DTOs
│   │   │   │   ├── repository/         # JPA Repositories
│   │   │   │   ├── service/            # Business Logic
│   │   │   │   │   ├── inter/          # Service Interfaces
│   │   │   │   │   └── impl/           # Service Implementations
│   │   │   │   ├── security/           # JWT & Security
│   │   │   │   ├── mapper/             # MapStruct Mappers
│   │   │   │   └── enumEntity/         # Enums
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── application-prod.properties
│   │   └── test/
│   └── pom.xml
│
└── sportshope-commerceplatform-fe/     # Frontend (React)
    ├── src/
    │   ├── components/                 # React Components
    │   │   ├── auth/                   # Authentication dialogs
    │   │   ├── common/                 # Common components
    │   │   ├── home/                   # Homepage components
    │   │   ├── layout/                 # Layout (Header, Footer)
    │   │   ├── product/                # Product components
    │   │   ├── products/               # Product listing
    │   │   └── ui/                     # shadcn/ui components
    │   ├── features/                   # Feature modules
    │   │   ├── admin/                  # Admin features
    │   │   ├── auth/                   # Auth features
    │   │   ├── cart/                   # Cart features
    │   │   └── customer/               # Customer features
    │   ├── pages/                      # Page components
    │   ├── services/                   # API services (14 services)
    │   ├── hooks/                      # Custom React hooks
    │   ├── store/                      # Zustand stores
    │   ├── types/                      # TypeScript types
    │   └── utils/                      # Utility functions
    ├── package.json
    └── vite.config.ts
```

## 💻 Yêu cầu hệ thống

### Backend
- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+
- AWS Account (cho các dịch vụ cloud)

### Frontend
- Node.js 18+ hoặc cao hơn
- npm hoặc yarn

## 🚀 Cài đặt và chạy dự án

### 1. Clone repository

```bash
git clone <repository-url>
cd sport-shop
```

### 2. Backend Setup

```bash
cd sportshope-commerceplatform-be

# Cấu hình database trong application.properties
# Sửa các thông tin: spring.datasource.url, username, password

# Build và chạy
./mvnw spring-boot:run
# Hoặc trên Windows:
mvnw.cmd spring-boot:run

# Backend sẽ chạy tại: http://localhost:8080
```

### 3. Frontend Setup

```bash
cd sportshope-commerceplatform-fe

# Cài đặt dependencies
npm install

# Tạo file .env (xem phần Cấu hình môi trường)
# Chạy development server
npm run dev

# Frontend sẽ chạy tại: http://localhost:5173
```

### 4. Build Production

**Backend:**
```bash
cd sportshope-commerceplatform-be
./mvnw clean package
# File JAR sẽ được tạo tại: target/application.jar
```

**Frontend:**
```bash
cd sportshope-commerceplatform-fe
npm run build
# Files sẽ được tạo tại: dist/
```

## ⚙️ Cấu hình môi trường

### Backend (`application.properties`)

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/sportshop_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT (tùy chỉnh theo nhu cầu)
jwt.secret=your-secret-key
jwt.expiration=86400000

# File Storage (Local hoặc S3)
storage.type=local
upload.base-dir=uploads

# AWS S3 (nếu dùng S3)
# storage.type=s3
# aws.s3.bucket-name=your-bucket-name
# aws.s3.base-url=https://your-bucket.s3.region.amazonaws.com

# AWS Region
spring.cloud.aws.region.static=ap-southeast-1
```

### Frontend (`.env`)

Tạo file `.env` trong thư mục `sportshope-commerceplatform-fe`:

```env
VITE_API_URL=http://localhost:8080
```

## 📚 API Documentation

### Authentication Endpoints

- `POST /api/auth/request-otp` - Yêu cầu OTP (Email/SMS)
- `POST /api/auth/verify-otp` - Xác thực OTP
- `POST /api/auth/resend-otp` - Gửi lại OTP
- `POST /api/auth/refresh-token` - Refresh access token
- `GET /api/auth/me` - Lấy thông tin user hiện tại
- `POST /api/auth/logout` - Đăng xuất

### Product Endpoints

- `GET /api/products` - Lấy danh sách sản phẩm (có phân trang, filter)
- `GET /api/products/:slug` - Lấy chi tiết sản phẩm
- `GET /api/products/search` - Tìm kiếm sản phẩm

### Cart Endpoints

- `GET /api/cart` - Lấy giỏ hàng
- `POST /api/cart/items` - Thêm sản phẩm vào giỏ
- `PUT /api/cart/items/:itemId` - Cập nhật số lượng
- `DELETE /api/cart/items/:itemId` - Xóa sản phẩm khỏi giỏ

### Order Endpoints

- `POST /api/orders` - Tạo đơn hàng
- `GET /api/orders` - Lấy danh sách đơn hàng
- `GET /api/orders/:id` - Lấy chi tiết đơn hàng

### Admin Endpoints

- `POST /api/admin/products` - Tạo sản phẩm
- `PUT /api/admin/products/:id` - Cập nhật sản phẩm
- `DELETE /api/admin/products/:id` - Xóa sản phẩm
- `GET /api/admin/orders` - Quản lý đơn hàng
- `GET /api/admin/users` - Quản lý người dùng

### Chat Endpoints

- `GET /api/chat/rooms` - Lấy danh sách phòng chat
- `GET /api/chat/rooms/:roomId/messages` - Lấy tin nhắn
- `POST /api/chat/rooms/:roomId/messages` - Gửi tin nhắn
- `POST /api/chat/files/upload` - Upload file chat

**WebSocket**: `/ws/chat` - Kết nối WebSocket cho chat real-time

## 🗄️ Cấu trúc Database

### Các Entities chính:

- **Product**: Sản phẩm chính
- **ProductVariant**: Biến thể sản phẩm (màu, size, giá, stock)
- **ProductCategory**: Liên kết sản phẩm với danh mục
- **ProductSport**: Liên kết sản phẩm với môn thể thao
- **ProductAudience**: Liên kết sản phẩm với đối tượng
- **Category**: Danh mục (hỗ trợ 3 cấp)
- **Brand**: Thương hiệu
- **Sport**: Môn thể thao
- **Audience**: Đối tượng (Nam, Nữ, Trẻ em...)
- **Attribute & AttributeValue**: Thuộc tính sản phẩm
- **Color & Size**: Màu sắc và kích thước
- **Cart & CartItem**: Giỏ hàng
- **Order & OrderItem**: Đơn hàng
- **User**: Người dùng
- **UserAddress & UserPhone**: Địa chỉ và số điện thoại
- **ChatRoom & ChatMessage**: Phòng chat và tin nhắn

## ☁️ Deployment

### Backend Deployment

1. **Build JAR file:**
```bash
./mvnw clean package -DskipTests
```

2. **Chạy với production profile:**
```bash
java -jar -Dspring.profiles.active=prod target/application.jar
```

3. **AWS Configuration:**
   - Cấu hình AWS credentials
   - Setup Parameter Store cho production config
   - Cấu hình S3 bucket cho file storage

### Frontend Deployment

1. **Build production:**
```bash
npm run build
```

2. **Deploy `dist/` folder:**
   - Có thể deploy lên Vercel, Netlify, AWS S3 + CloudFront, hoặc bất kỳ static hosting nào

## 🤝 Đóng góp

1. Fork dự án
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

## 📝 License

Dự án này được phát triển bởi OJT Team AWS.

## 👥 Authors

- OJT Team AWS

## 📞 Liên hệ

Nếu có câu hỏi hoặc đề xuất, vui lòng tạo issue trên repository.

---

**Lưu ý**: Đảm bảo đã cấu hình đúng database và AWS credentials trước khi chạy dự án.

