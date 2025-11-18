# 快速开始指南 (Quick Start Guide)

本指南帮助你在本地环境快速启动考研学习平台。

## 前置条件

确保已安装以下软件：

- **JDK 17** 或更高版本
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+** (可选，用于缓存功能)
- **腾讯云 COS 账号** (可选，用于文件存储)

## 步骤 1: 克隆项目

```bash
git clone https://github.com/29jdd1d/styduapp3.git
cd styduapp3
```

## 步骤 2: 配置 MySQL 数据库

1. 启动 MySQL 服务

2. 创建数据库：
```sql
CREATE DATABASE exam_platform DEFAULT CHARSET=utf8mb4;
```

3. (可选) 执行初始化 SQL：
```sql
USE exam_platform;

-- 初始化分类数据
INSERT INTO categories (name, type) VALUES
('考研政治', 'politics'),
('考研英语', 'english'),
('考研数学', 'math'),
('专业课', 'professional');
```

## 步骤 3: 配置 Redis (可选但推荐)

启动 Redis 服务：
```bash
# Linux/Mac
redis-server

# Windows
redis-server.exe
```

默认配置使用 localhost:6379，无密码。

## 步骤 4: 配置应用

### 方式一：修改 application.yml

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_platform?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root        # 修改为你的数据库用户名
    password: your_password  # 修改为你的数据库密码
```

### 方式二：使用环境变量 (推荐)

创建 `.env` 文件或设置环境变量：

```bash
# 数据库配置
export DB_URL=jdbc:mysql://localhost:3306/exam_platform
export DB_USERNAME=root
export DB_PASSWORD=your_password

# 腾讯云 COS 配置 (可选)
export TENCENT_COS_SECRET_ID=your-secret-id
export TENCENT_COS_SECRET_KEY=your-secret-key
export TENCENT_COS_BUCKET=your-bucket-name

# JWT 密钥 (生产环境必须修改)
export JWT_SECRET=your-very-long-secret-key-at-least-32-characters
```

### 方式三：创建本地配置文件

复制示例配置：
```bash
cp src/main/resources/application-example.yml src/main/resources/application-local.yml
```

编辑 `application-local.yml` 填入你的配置，然后使用：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## 步骤 5: 构建项目

```bash
mvn clean install
```

## 步骤 6: 运行应用

### 使用 Maven 运行
```bash
mvn spring-boot:run
```

### 使用 JAR 包运行
```bash
java -jar target/exam-platform-1.0.0.jar
```

应用将在 `http://localhost:8080/v1` 启动。

## 步骤 7: 测试 API

### 健康检查

由于启用了 Spring Security，首次访问会要求登录。查看控制台输出的生成密码：

```
Using generated security password: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

### 测试文件上传 (需要配置 COS)

```bash
curl -X POST http://localhost:8080/v1/upload \
  -u user:生成的密码 \
  -F "file=@test.jpg" \
  -F "type=image"
```

### 测试资源 API

```bash
# 获取资源列表
curl http://localhost:8080/v1/resources?categoryId=1&page=1&pageSize=20 \
  -u user:生成的密码
```

## 常见问题

### 1. 连接 MySQL 失败

**错误**: `Communications link failure`

**解决**:
- 确认 MySQL 服务已启动
- 检查用户名和密码是否正确
- 确认数据库 `exam_platform` 已创建

### 2. Redis 连接失败

**错误**: `Unable to connect to Redis`

**解决**:
- 如果不需要 Redis，可以暂时禁用相关配置
- 确认 Redis 服务已启动：`redis-cli ping` 应返回 `PONG`
- 检查 Redis 端口和密码配置

### 3. 腾讯云 COS 配置问题

**解决**:
- 文件上传功能需要有效的腾讯云 COS 凭证
- 可以暂时跳过文件上传测试
- 确保 SecretId、SecretKey 和 Bucket 配置正确

### 4. 端口已被占用

**错误**: `Port 8080 is already in use`

**解决**:
修改 `application.yml` 中的端口：
```yaml
server:
  port: 8081  # 改为其他端口
```

## 下一步

1. **禁用 Spring Security** (开发阶段)：
   - 创建 SecurityConfig 类禁用安全认证
   - 或实现完整的 JWT 认证

2. **实现更多功能**：
   - 用户认证 (微信登录)
   - 学习进度跟踪
   - 题库系统
   - 社区功能

3. **生产部署**：
   - 修改所有默认密钥
   - 使用 HTTPS
   - 配置生产数据库
   - 设置日志级别

## 开发工具推荐

- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **API 测试**: Postman / Insomnia
- **数据库管理**: MySQL Workbench / DBeaver
- **Redis 管理**: Redis Desktop Manager

## 需要帮助？

- 查看 [README.md](README.md) 了解详细文档
- 查看 [api_design.md](api_design.md) 了解 API 设计
- 查看 [database_design_simple.md](database_design_simple.md) 了解数据库设计
- 提交 Issue 到 GitHub 仓库
