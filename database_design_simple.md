# 考研学习平台数据库设计（简化版）

## 1. 用户模块

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    openid VARCHAR(64) UNIQUE NOT NULL COMMENT '微信OpenID',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像',
    phone VARCHAR(20) COMMENT '手机号',
    target_school VARCHAR(100) COMMENT '目标院校',
    target_major VARCHAR(100) COMMENT '目标专业',
    exam_year INT COMMENT '考研年份',
    study_days INT DEFAULT 0 COMMENT '学习天数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

## 2. 资源模块

### 资源分类表 (categories)
```sql
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    type ENUM('politics', 'english', 'math', 'professional') COMMENT '科目类型',
    parent_id INT DEFAULT 0 COMMENT '父分类ID',
    sort INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源分类';
```

### 学习资源表 (resources)
```sql
CREATE TABLE resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    type ENUM('video', 'document') NOT NULL COMMENT '资源类型',
    title VARCHAR(200) NOT NULL,
    teacher VARCHAR(50) COMMENT '讲师',
    cover VARCHAR(500) COMMENT '封面',
    url VARCHAR(500) NOT NULL COMMENT '资源URL',
    duration INT COMMENT '时长(秒)',
    view_count INT DEFAULT 0 COMMENT '观看数',
    status TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习资源';
```

### 学习记录表 (study_records)
```sql
CREATE TABLE study_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    progress INT DEFAULT 0 COMMENT '进度百分比',
    last_position INT DEFAULT 0 COMMENT '最后学习位置',
    is_completed TINYINT DEFAULT 0 COMMENT '是否完成',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_resource (user_id, resource_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录';
```

## 3. 学习计划模块

### 学习计划表 (study_plans)
```sql
CREATE TABLE study_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    subject ENUM('politics', 'english', 'math', 'professional') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    target_score INT COMMENT '目标分数',
    progress INT DEFAULT 0 COMMENT '完成进度',
    status TINYINT DEFAULT 1 COMMENT '状态 0-放弃 1-进行中 2-完成',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划';
```

### 计划任务表 (plan_tasks)
```sql
CREATE TABLE plan_tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    task_date DATE NOT NULL,
    title VARCHAR(200) NOT NULL,
    resource_id BIGINT COMMENT '关联资源',
    is_completed TINYINT DEFAULT 0,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_plan (plan_id),
    INDEX idx_date (task_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划任务';
```

## 4. 题库模块

### 题目表 (questions)
```sql
CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject ENUM('politics', 'english', 'math', 'professional') NOT NULL,
    type ENUM('single', 'multiple', 'judge', 'fill', 'answer') NOT NULL COMMENT '题型',
    chapter VARCHAR(100) COMMENT '章节',
    year INT COMMENT '年份',
    difficulty TINYINT DEFAULT 2 COMMENT '难度 1-简单 2-中等 3-困难',
    content TEXT NOT NULL COMMENT '题目内容',
    options JSON COMMENT '选项',
    answer TEXT NOT NULL COMMENT '答案',
    analysis TEXT COMMENT '解析',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_subject (subject),
    INDEX idx_type (type),
    INDEX idx_year (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目';
```

### 答题记录表 (answer_records)
```sql
CREATE TABLE answer_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    user_answer TEXT,
    is_correct TINYINT COMMENT '是否正确',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题记录';
```

### 错题本表 (wrong_questions)
```sql
CREATE TABLE wrong_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    wrong_count INT DEFAULT 1 COMMENT '错误次数',
    is_mastered TINYINT DEFAULT 0 COMMENT '是否掌握',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_question (user_id, question_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本';
```

## 5. 社区模块

### 文章表 (articles)
```sql
CREATE TABLE articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('news', 'experience', 'tips') NOT NULL COMMENT '类型',
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    images JSON COMMENT '图片',
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '状态 0-草稿 1-发布',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章';
```

### 评论表 (comments)
```sql
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID',
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_article (article_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论';
```

### 打卡表 (punch_records)
```sql
CREATE TABLE punch_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    punch_date DATE NOT NULL,
    duration INT DEFAULT 0 COMMENT '学习时长(分钟)',
    content VARCHAR(500) COMMENT '学习内容',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, punch_date),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录';
```

## 6. 后台管理模块

### 管理员表 (admins)
```sql
CREATE TABLE admins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    name VARCHAR(50) COMMENT '姓名',
    role ENUM('super', 'admin', 'editor') DEFAULT 'editor' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员';
```

### 操作日志表 (logs)
```sql
CREATE TABLE logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT,
    action VARCHAR(100) COMMENT '操作',
    module VARCHAR(50) COMMENT '模块',
    ip VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_admin (admin_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
```

## 初始化SQL

```sql
-- 创建数据库
CREATE DATABASE exam_platform DEFAULT CHARSET=utf8mb4;

USE exam_platform;

-- 执行上述建表语句

-- 初始化分类数据
INSERT INTO categories (name, type) VALUES
('考研政治', 'politics'),
('考研英语', 'english'),
('考研数学', 'math'),
('专业课', 'professional');

-- 初始化管理员
INSERT INTO admins (username, password, name, role) VALUES
('admin', MD5('admin123'), '系统管理员', 'super');
```