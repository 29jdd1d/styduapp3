# 考研学习平台 (Graduate Exam Study Platform)

基于 Java 17 和 Spring Boot 3.x 开发的考研学习平台后端服务。

## 技术栈

- **Java 17** - 编程语言
- **Spring Boot 3.1.5** - 应用框架
- **Spring Data JPA** - 数据持久化
- **MySQL** - 关系型数据库
- **Redis** - 缓存和会话管理
- **Tencent COS** - 对象存储服务
- **JWT** - 用户认证
- **Maven** - 项目管理

## 核心功能

### 1. 文件存储 (Tencent COS)
- 支持图片、视频、文档上传
- 自动生成唯一文件名
- 支持文件删除
- 返回文件访问 URL

### 2. Redis 缓存
- 资源数据缓存
- 浏览次数计数器
- 支持多种数据结构（String, Hash, List, Set, Sorted Set）
- 自动序列化/反序列化

### 3. 数据库设计
详见 `database_design_simple.md`，包含：
- 用户管理
- 学习资源
- 学习计划
- 题库系统
- 社区模块

### 4. API 接口
详见 `api_design.md`，提供：
- 用户认证
- 资源管理
- 学习计划
- 题库练习
- 社区互动

## 项目结构

```
src/
├── main/
│   ├── java/com/exam/platform/
│   │   ├── ExamPlatformApplication.java  # 主应用类
│   │   ├── common/                        # 通用类
│   │   │   └── ApiResponse.java          # 统一响应格式
│   │   ├── config/                        # 配置类
│   │   │   ├── TencentCosConfig.java     # 腾讯云COS配置
│   │   │   ├── RedisConfig.java          # Redis配置
│   │   │   └── JwtConfig.java            # JWT配置
│   │   ├── entity/                        # 实体类
│   │   │   ├── User.java                 # 用户实体
│   │   │   ├── Resource.java             # 资源实体
│   │   │   └── Category.java             # 分类实体
│   │   ├── repository/                    # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   └── ResourceRepository.java
│   │   ├── service/                       # 服务层
│   │   │   ├── FileStorageService.java   # 文件存储服务
│   │   │   ├── RedisService.java         # Redis服务
│   │   │   └── ResourceService.java      # 资源服务
│   │   └── controller/                    # 控制器层
│   │       ├── UploadController.java     # 文件上传控制器
│   │       └── ResourceController.java   # 资源控制器
│   └── resources/
│       └── application.yml                # 应用配置
└── test/
    └── java/com/exam/platform/           # 测试代码
```

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Tencent Cloud COS 账号

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/29jdd1d/styduapp3.git
cd styduapp3
```

### 2. 配置数据库

创建数据库：
```sql
CREATE DATABASE exam_platform DEFAULT CHARSET=utf8mb4;
```

### 3. 配置 Redis

确保 Redis 服务已启动：
```bash
redis-server
```

### 4. 配置腾讯云 COS

在 `application.yml` 中配置或通过环境变量设置：

```yaml
tencent:
  cos:
    secret-id: your-secret-id
    secret-key: your-secret-key
    region: ap-guangzhou
    bucket: your-bucket-name
```

或使用环境变量：
```bash
export TENCENT_COS_SECRET_ID=your-secret-id
export TENCENT_COS_SECRET_KEY=your-secret-key
export TENCENT_COS_BUCKET=your-bucket-name
```

### 5. 修改应用配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_platform?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root
    password: your-password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: # Redis密码，如果没有设置则留空
```

### 6. 构建项目

```bash
mvn clean install
```

### 7. 运行项目

```bash
mvn spring-boot:run
```

或者：
```bash
java -jar target/exam-platform-1.0.0.jar
```

应用将在 `http://localhost:8080/v1` 启动

## 核心 API 示例

### 文件上传

```bash
curl -X POST http://localhost:8080/v1/upload \
  -F "file=@/path/to/file.jpg" \
  -F "type=image"
```

响应：
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://your-bucket.cos.ap-guangzhou.myqcloud.com/image/xxx.jpg",
    "size": 102400,
    "filename": "file.jpg"
  }
}
```

### 获取资源详情

```bash
curl http://localhost:8080/v1/resources/1
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "categoryId": 1,
    "type": "video",
    "title": "马原第一讲",
    "teacher": "张老师",
    "cover": "https://...",
    "url": "https://...",
    "duration": 3600,
    "viewCount": 1250,
    "status": 1
  }
}
```

### 获取资源浏览次数

```bash
curl http://localhost:8080/v1/resources/1/view-count
```

## Redis 使用示例

### 缓存资源数据

```java
@Autowired
private ResourceService resourceService;

// 资源被访问时自动增加浏览次数（存储在Redis）
resourceService.incrementViewCount(resourceId);

// 获取浏览次数（从Redis读取）
Long viewCount = resourceService.getViewCount(resourceId);

// 缓存自定义数据
resourceService.cacheResourceData(resourceId, data);

// 获取缓存数据
Object data = resourceService.getCachedResourceData(resourceId);
```

### 使用 RedisService

```java
@Autowired
private RedisService redisService;

// String 操作
redisService.set("key", "value");
redisService.set("key", "value", 1, TimeUnit.HOURS);
Object value = redisService.get("key");

// Hash 操作
redisService.hSet("user:1", "name", "张三");
Object name = redisService.hGet("user:1", "name");

// List 操作
redisService.lPush("list", "item");
List<Object> items = redisService.lRange("list", 0, -1);

// Set 操作
redisService.sAdd("set", "member1", "member2");
Set<Object> members = redisService.sMembers("set");
```

## 配置说明

### 数据库配置
- 使用 JPA 自动创建/更新表结构（`ddl-auto: update`）
- 支持 MySQL 8.0+
- UTF-8MB4 字符集支持 emoji

### Redis 配置
- 连接池配置（Lettuce）
- JSON 序列化
- 默认 TTL 1小时

### Tencent COS 配置
- 支持多地域
- 自动生成文件 URL
- 支持文件删除

### JWT 配置
- 默认过期时间 7 天
- 使用 HS256 算法
- Bearer Token 认证

## 开发建议

### 1. 本地开发环境

创建 `application-local.yml`（已在 .gitignore 中）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/exam_platform_dev
    username: dev
    password: dev123
  
  data:
    redis:
      host: localhost
      port: 6379

tencent:
  cos:
    secret-id: your-dev-secret-id
    secret-key: your-dev-secret-key
    bucket: your-dev-bucket
```

使用本地配置：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 2. 生产环境配置

使用环境变量覆盖敏感配置：
```bash
export TENCENT_COS_SECRET_ID=prod-secret-id
export TENCENT_COS_SECRET_KEY=prod-secret-key
export JWT_SECRET=production-secret-key-very-long
```

## 安全建议

1. **更改默认密钥**：修改 JWT secret 和数据库密码
2. **使用 HTTPS**：生产环境必须使用 HTTPS
3. **环境变量**：敏感信息使用环境变量
4. **权限控制**：实现完整的权限验证
5. **输入验证**：对所有输入进行验证
6. **SQL 注入**：使用参数化查询（JPA 已提供）

## 扩展功能

基础框架已搭建完成，可继续实现：

- [ ] 用户认证（微信登录、JWT）
- [ ] 学习进度跟踪
- [ ] 题库系统
- [ ] 学习计划管理
- [ ] 社区功能（文章、评论、打卡）
- [ ] 数据统计分析
- [ ] 管理后台

## 许可证

MIT License

## 联系方式

如有问题，请提交 Issue 或 Pull Request。
