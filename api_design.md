# 考研学习平台 API 接口设计文档

## 基础说明

### 接口规范
- **协议**: HTTPS
- **基础路径**: `https://api.example.com/v1`
- **数据格式**: JSON
- **字符编码**: UTF-8
- **认证方式**: JWT Token (请求头携带 `Authorization: Bearer {token}`)

### 统一响应格式

#### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

#### 失败响应
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

### 通用错误码
| 错误码 | 说明 |
|-------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或token过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 1. 用户管理模块

### 1.1 微信登录
**接口**: `POST /auth/wechat/login`

**请求参数**:
```json
{
  "code": "微信登录code"
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "openid": "oX1234567890",
      "nickname": "考研人",
      "avatar": "https://cdn.example.com/avatar.jpg",
      "is_new": true
    }
  }
}
```

---

### 1.2 获取用户信息
**接口**: `GET /user/profile`

**请求头**:
```
Authorization: Bearer {token}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "nickname": "考研人",
    "avatar": "https://cdn.example.com/avatar.jpg",
    "phone": "13800138000",
    "target_school": "清华大学",
    "target_major": "计算机科学与技术",
    "exam_year": 2026,
    "study_days": 120,
    "created_at": "2025-01-01 08:00:00"
  }
}
```

---

### 1.3 更新用户信息
**接口**: `PUT /user/profile`

**请求参数**:
```json
{
  "nickname": "新昵称",
  "phone": "13800138000",
  "target_school": "清华大学",
  "target_major": "计算机科学与技术",
  "exam_year": 2026
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

---

### 1.4 学习数据看板
**接口**: `GET /user/study-stats`

**请求参数**:
```
?period=week  // week-本周, month-本月, all-全部
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total_study_days": 120,
    "continuous_days": 15,
    "total_duration": 72000,
    "video_count": 85,
    "question_count": 1250,
    "correct_rate": 75.5,
    "daily_stats": [
      {
        "date": "2025-11-12",
        "duration": 3600,
        "question_count": 50,
        "video_count": 3
      }
    ],
    "subject_stats": [
      {
        "subject": "politics",
        "name": "政治",
        "duration": 20000,
        "progress": 65
      }
    ]
  }
}
```

---

## 2. 学习资源管理模块

### 2.1 获取资源分类
**接口**: `GET /resources/categories`

**请求参数**:
```
?type=politics  // politics, english, math, professional (可选)
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "考研政治",
      "type": "politics",
      "parent_id": 0,
      "children": [
        {
          "id": 11,
          "name": "马克思主义基本原理",
          "type": "politics",
          "parent_id": 1
        }
      ]
    }
  ]
}
```

---

### 2.2 获取资源列表
**接口**: `GET /resources`

**请求参数**:
```
?category_id=1      // 分类ID (可选)
?type=video         // video, document (可选)
?keyword=关键词     // 搜索关键词 (可选)
?page=1             // 页码
?page_size=20       // 每页数量
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "page_size": 20,
    "list": [
      {
        "id": 1,
        "category_id": 11,
        "category_name": "马克思主义基本原理",
        "type": "video",
        "title": "马原第一讲：哲学基础",
        "teacher": "张老师",
        "cover": "https://cdn.example.com/cover.jpg",
        "duration": 3600,
        "view_count": 1250,
        "progress": 45,
        "is_completed": false,
        "created_at": "2025-10-01 10:00:00"
      }
    ]
  }
}
```

---

### 2.3 获取资源详情
**接口**: `GET /resources/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "category_id": 11,
    "category_name": "马克思主义基本原理",
    "type": "video",
    "title": "马原第一讲：哲学基础",
    "teacher": "张老师",
    "cover": "https://cdn.example.com/cover.jpg",
    "url": "https://cdn.example.com/video.mp4",
    "duration": 3600,
    "view_count": 1250,
    "progress": 45,
    "last_position": 1620,
    "is_completed": false,
    "created_at": "2025-10-01 10:00:00"
  }
}
```

---

### 2.4 更新学习进度
**接口**: `POST /resources/{id}/progress`

**请求参数**:
```json
{
  "progress": 50,
  "last_position": 1800,
  "is_completed": false
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "进度已更新",
  "data": null
}
```

---

### 2.5 获取学习记录
**接口**: `GET /user/study-records`

**请求参数**:
```
?type=video         // video, document (可选)
?is_completed=1     // 0-未完成, 1-已完成 (可选)
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "list": [
      {
        "id": 1,
        "resource_id": 1,
        "resource_title": "马原第一讲",
        "resource_type": "video",
        "progress": 100,
        "is_completed": true,
        "updated_at": "2025-11-15 20:30:00"
      }
    ]
  }
}
```

---

## 3. 学习计划模块

### 3.1 创建学习计划
**接口**: `POST /study-plans`

**请求参数**:
```json
{
  "name": "政治基础阶段计划",
  "subject": "politics",
  "start_date": "2025-11-18",
  "end_date": "2025-12-31",
  "target_score": 75
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "计划创建成功",
  "data": {
    "id": 1
  }
}
```

---

### 3.2 获取计划列表
**接口**: `GET /study-plans`

**请求参数**:
```
?status=1           // 0-放弃, 1-进行中, 2-完成 (可选)
?subject=politics   // 科目 (可选)
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "政治基础阶段计划",
      "subject": "politics",
      "subject_name": "考研政治",
      "start_date": "2025-11-18",
      "end_date": "2025-12-31",
      "target_score": 75,
      "progress": 35,
      "status": 1,
      "total_tasks": 50,
      "completed_tasks": 18,
      "created_at": "2025-11-18 06:44:46"
    }
  ]
}
```

---

### 3.3 获取计划详情
**接口**: `GET /study-plans/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "政治基础阶段计划",
    "subject": "politics",
    "subject_name": "考研政治",
    "start_date": "2025-11-18",
    "end_date": "2025-12-31",
    "target_score": 75,
    "progress": 35,
    "status": 1,
    "total_tasks": 50,
    "completed_tasks": 18,
    "tasks_today": [
      {
        "id": 1,
        "title": "观看马原第一讲",
        "resource_id": 1,
        "task_date": "2025-11-18",
        "is_completed": false
      }
    ],
    "created_at": "2025-11-18 06:44:46"
  }
}
```

---

### 3.4 更新学习计划
**接口**: `PUT /study-plans/{id}`

**请求参数**:
```json
{
  "name": "政治基础阶段计划(修改)",
  "end_date": "2026-01-15",
  "target_score": 80
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

---

### 3.5 删除学习计划
**接口**: `DELETE /study-plans/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 3.6 添加计划任务
**接口**: `POST /study-plans/{id}/tasks`

**请求参数**:
```json
{
  "task_date": "2025-11-19",
  "title": "观看英语视频",
  "resource_id": 25
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "任务添加成功",
  "data": {
    "id": 101
  }
}
```

---

### 3.7 获取任务列表
**接口**: `GET /study-plans/{id}/tasks`

**请求参数**:
```
?date=2025-11-18    // 日期 (可选)
?is_completed=0     // 完成状态 (可选)
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "plan_id": 1,
      "task_date": "2025-11-18",
      "title": "观看马原第一讲",
      "resource_id": 1,
      "resource_title": "马原第一讲：哲学基础",
      "is_completed": false,
      "completed_at": null
    }
  ]
}
```

---

### 3.8 完成任务
**接口**: `POST /study-plans/tasks/{task_id}/complete`

**响应数据**:
```json
{
  "code": 200,
  "message": "任务已完成",
  "data": null
}
```

---

## 4. 题库模块

### 4.1 获取题目列表
**接口**: `GET /questions`

**请求参数**:
```
?subject=politics   // 科目 (必填)
?type=single        // 题型 (可选)
?chapter=第一章     // 章节 (可选)
?year=2024          // 年份 (可选)
?difficulty=2       // 难度 (可选)
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 500,
    "page": 1,
    "page_size": 20,
    "list": [
      {
        "id": 1,
        "subject": "politics",
        "type": "single",
        "chapter": "马克思主义基本原理",
        "year": 2024,
        "difficulty": 2,
        "content": "马克思主义哲学的基本问题是？",
        "options": [
          {"key": "A", "value": "物质和意识的关系问题"},
          {"key": "B", "value": "生产力和生产关系的问题"},
          {"key": "C", "value": "经济基础和上层建筑的问题"},
          {"key": "D", "value": "理论和实践的问题"}
        ]
      }
    ]
  }
}
```

---

### 4.2 获取题目详情
**接口**: `GET /questions/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "subject": "politics",
    "type": "single",
    "chapter": "马克思主义基本原理",
    "year": 2024,
    "difficulty": 2,
    "content": "马克思主义哲学的基本问题是？",
    "options": [
      {"key": "A", "value": "物质和意识的关系问题"},
      {"key": "B", "value": "生产力和生产关系的问题"},
      {"key": "C", "value": "经济基础和上层建筑的问题"},
      {"key": "D", "value": "理论和实践的问题"}
    ],
    "answer": "A",
    "analysis": "马克思主义哲学的基本问题是物质和意识的关系问题...",
    "user_answer": null,
    "is_correct": null,
    "is_wrong": false
  }
}
```

---

### 4.3 提交答案
**接口**: `POST /questions/{id}/answer`

**请求参数**:
```json
{
  "answer": "A"
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "提交成功",
  "data": {
    "is_correct": true,
    "correct_answer": "A",
    "analysis": "马克思主义哲学的基本问题是物质和意识的关系问题..."
  }
}
```

---

### 4.4 获取错题本
**接口**: `GET /questions/wrong`

**请求参数**:
```
?subject=politics   // 科目 (可选)
?is_mastered=0      // 是否已掌握 (可选)
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 35,
    "list": [
      {
        "id": 1,
        "question_id": 100,
        "question": {
          "id": 100,
          "subject": "politics",
          "type": "single",
          "content": "题目内容...",
          "difficulty": 3
        },
        "wrong_count": 2,
        "is_mastered": false,
        "created_at": "2025-11-10 15:30:00"
      }
    ]
  }
}
```

---

### 4.5 标记错题已掌握
**接口**: `POST /questions/wrong/{id}/master`

**响应数据**:
```json
{
  "code": 200,
  "message": "已标记为掌握",
  "data": null
}
```

---

### 4.6 智能练习
**接口**: `GET /questions/practice`

**请求参数**:
```
?subject=politics   // 科目 (必填)
?mode=wrong         // wrong-错题, weak-薄弱, random-随机
?count=20           // 题目数量
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "practice_id": 1001,
    "questions": [
      {
        "id": 1,
        "content": "题目内容...",
        "options": [],
        "type": "single"
      }
    ]
  }
}
```

---

## 5. 社区模块

### 5.1 获取文章列表
**接口**: `GET /articles`

**请求参数**:
```
?type=experience    // news, experience, tips (可选)
?keyword=关键词     // 搜索 (可选)
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 200,
    "list": [
      {
        "id": 1,
        "user_id": 10,
        "user": {
          "id": 10,
          "nickname": "学霸小王",
          "avatar": "https://cdn.example.com/avatar.jpg"
        },
        "type": "experience",
        "title": "我的考研上岸经验分享",
        "content": "文章内容摘要...",
        "images": ["https://cdn.example.com/1.jpg"],
        "view_count": 1500,
        "like_count": 85,
        "comment_count": 23,
        "is_liked": false,
        "created_at": "2025-11-15 10:00:00"
      }
    ]
  }
}
```

---

### 5.2 获取文章详情
**接口**: `GET /articles/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "user": {
      "id": 10,
      "nickname": "学霸小王",
      "avatar": "https://cdn.example.com/avatar.jpg"
    },
    "type": "experience",
    "title": "我的考研上岸经验分享",
    "content": "完整文章内容...",
    "images": ["https://cdn.example.com/1.jpg"],
    "view_count": 1500,
    "like_count": 85,
    "comment_count": 23,
    "is_liked": false,
    "created_at": "2025-11-15 10:00:00"
  }
}
```

---

### 5.3 发布文章
**接口**: `POST /articles`

**请求参数**:
```json
{
  "type": "experience",
  "title": "我的考研上岸经验分享",
  "content": "文章内容...",
  "images": ["https://cdn.example.com/1.jpg"],
  "status": 1
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1
  }
}
```

---

### 5.4 更新文章
**接口**: `PUT /articles/{id}`

**请求参数**:
```json
{
  "title": "修改后的标题",
  "content": "修改后的内容..."
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

---

### 5.5 删除文章
**接口**: `DELETE /articles/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 5.6 点赞文章
**接口**: `POST /articles/{id}/like`

**响应数据**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "like_count": 86
  }
}
```

---

### 5.7 取消点赞
**接口**: `DELETE /articles/{id}/like`

**响应数据**:
```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": {
    "like_count": 85
  }
}
```

---

### 5.8 获取评论列表
**接口**: `GET /articles/{id}/comments`

**请求参数**:
```
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 23,
    "list": [
      {
        "id": 1,
        "article_id": 1,
        "user": {
          "id": 15,
          "nickname": "考研加油",
          "avatar": "https://cdn.example.com/avatar2.jpg"
        },
        "parent_id": 0,
        "content": "写得太好了！",
        "like_count": 5,
        "replies": [
          {
            "id": 2,
            "user": {
              "id": 10,
              "nickname": "学霸小王",
              "avatar": "https://cdn.example.com/avatar.jpg"
            },
            "reply_to_user_id": 15,
            "reply_to_user": "考研加油",
            "content": "谢谢支持！",
            "created_at": "2025-11-15 11:00:00"
          }
        ],
        "created_at": "2025-11-15 10:30:00"
      }
    ]
  }
}
```

---

### 5.9 发表评论
**接口**: `POST /articles/{id}/comments`

**请求参数**:
```json
{
  "content": "评论内容",
  "parent_id": 0,
  "reply_to_user_id": null
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 100
  }
}
```

---

### 5.10 删除评论
**接口**: `DELETE /comments/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 5.11 学习打卡
**接口**: `POST /punch`

**请求参数**:
```json
{
  "punch_date": "2025-11-18",
  "duration": 120,
  "content": "今天学习了马原和英语"
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "打卡成功",
  "data": {
    "continuous_days": 16
  }
}
```

---

### 5.12 获取打卡记录
**接口**: `GET /punch`

**请求参数**:
```
?user_id=1          // 用户ID (可选，不传则查看自己)
?month=2025-11      // 月份 (可选)
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "continuous_days": 16,
    "total_days": 45,
    "records": [
      {
        "id": 1,
        "punch_date": "2025-11-18",
        "duration": 120,
        "content": "今天学习了马原和英语",
        "created_at": "2025-11-18 22:30:00"
      }
    ]
  }
}
```

---

## 6. 后台管理模块

### 6.1 管理员登录
**接口**: `POST /admin/auth/login`

**请求参数**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "admin": {
      "id": 1,
      "username": "admin",
      "name": "系统管理员",
      "role": "super"
    }
  }
}
```

---

### 6.2 获取用户列表
**接口**: `GET /admin/users`

**请求参数**:
```
?keyword=关键词     // 昵称/手机号
?exam_year=2026     // 考研年份
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 1000,
    "list": [
      {
        "id": 1,
        "nickname": "考研人",
        "phone": "13800138000",
        "target_school": "清华大学",
        "target_major": "计算机",
        "exam_year": 2026,
        "study_days": 120,
        "created_at": "2025-01-01 08:00:00"
      }
    ]
  }
}
```

---

### 6.3 获取资源列表（后台）
**接口**: `GET /admin/resources`

**请求参数**:
```
?category_id=1
?type=video
?status=1
?keyword=关键词
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 500,
    "list": [
      {
        "id": 1,
        "category_id": 11,
        "category_name": "马克思主义基本原理",
        "type": "video",
        "title": "马原第一讲",
        "teacher": "张老师",
        "view_count": 1250,
        "status": 1,
        "created_at": "2025-10-01 10:00:00"
      }
    ]
  }
}
```

---

### 6.4 创建资源
**接口**: `POST /admin/resources`

**请求参数**:
```json
{
  "category_id": 11,
  "type": "video",
  "title": "马原第一讲",
  "teacher": "张老师",
  "cover": "https://cdn.example.com/cover.jpg",
  "url": "https://cdn.example.com/video.mp4",
  "duration": 3600,
  "status": 1
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1
  }
}
```

---

### 6.5 更新资源
**接口**: `PUT /admin/resources/{id}`

**请求参数**:
```json
{
  "title": "修改后的标题",
  "status": 0
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

---

### 6.6 删除资源
**接口**: `DELETE /admin/resources/{id}`

**响应数据**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 6.7 获取题目列表（后台）
**接口**: `GET /admin/questions`

**请求参数**:
```
?subject=politics
?type=single
?keyword=关键词
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 5000,
    "list": [
      {
        "id": 1,
        "subject": "politics",
        "type": "single",
        "chapter": "马克思主义基本原理",
        "difficulty": 2,
        "content": "题目内容...",
        "created_at": "2025-10-01 10:00:00"
      }
    ]
  }
}
```

---

### 6.8 创建题目
**接口**: `POST /admin/questions`

**请求参数**:
```json
{
  "subject": "politics",
  "type": "single",
  "chapter": "马克思主义基本原理",
  "year": 2024,
  "difficulty": 2,
  "content": "题目内容",
  "options": [
    {"key": "A", "value": "选项A"},
    {"key": "B", "value": "选项B"}
  ],
  "answer": "A",
  "analysis": "答案解析"
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1
  }
}
```

---

### 6.9 批量导入题目
**接口**: `POST /admin/questions/import`

**请求参数**: 
```
FormData:
file: Excel文件
```

**响应数据**:
```json
{
  "code": 200,
  "message": "导入成功",
  "data": {
    "success_count": 100,
    "error_count": 0
  }
}
```

---

### 6.10 数据统计
**接口**: `GET /admin/statistics`

**请求参数**:
```
?start_date=2025-11-01
?end_date=2025-11-18
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "user_stats": {
      "total_users": 10000,
      "new_users": 500,
      "active_users": 3000
    },
    "resource_stats": {
      "total_videos": 500,
      "total_documents": 300,
      "total_views": 50000
    },
    "question_stats": {
      "total_questions": 5000,
      "total_answers": 100000,
      "avg_accuracy": 72.5
    },
    "community_stats": {
      "total_articles": 1000,
      "total_comments": 5000,
      "total_punches": 80000
    }
  }
}
```

---

### 6.11 获取文章列表（后台）
**接口**: `GET /admin/articles`

**请求参数**:
```
?type=experience
?status=1
?keyword=关键词
?page=1
?page_size=20
```

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 1000,
    "list": [
      {
        "id": 1,
        "user": {
          "id": 10,
          "nickname": "学霸小王"
        },
        "type": "experience",
        "title": "我的考研上岸经验分享",
        "view_count": 1500,
        "like_count": 85,
        "status": 1,
        "created_at": "2025-11-15 10:00:00"
      }
    ]
  }
}
```

---

### 6.12 审核/下架文章
**接口**: `PUT /admin/articles/{id}/status`

**请求参数**:
```json
{
  "status": 0
}
```

**响应数据**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 7. 通用接口

### 7.1 文件上传
**接口**: `POST /upload`

**请求参数**: 
```
FormData:
file: 文件
type: image / video / document
```

**响应数据**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "url": "https://cdn.example.com/file.jpg",
    "size": 102400,
    "filename": "file.jpg"
  }
}
```

---

### 7.2 获取配置
**接口**: `GET /config`

**响应数据**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "app_name": "考研学习平台",
    "version": "1.0.0",
    "exam_subjects": [
      {"value": "politics", "label": "考研政治"},
      {"value": "english", "label": "考研英语"},
      {"value": "math", "label": "考研数学"},
      {"value": "professional", "label": "专业课"}
    ]
  }
}
```

---

## 附录

### A. 枚举值说明

#### 科目类型 (subject)
- `politics` - 考研政治
- `english` - 考研英语
- `math` - 考研数学
- `professional` - 专业课

#### 资源类型 (resource_type)
- `video` - 视频
- `document` - 文档

#### 题型 (question_type)
- `single` - 单选题
- `multiple` - 多选题
- `judge` - 判断题
- `fill` - 填空题
- `answer` - 简答题

#### 难度 (difficulty)
- `1` - 简单
- `2` - 中等
- `3` - 困难

#### 文章类型 (article_type)
- `news` - 资讯
- `experience` - 经验分享
- `tips` - 备考技巧

#### 管理员角色 (admin_role)
- `super` - 超级管理员
- `admin` - 管理员
- `editor` - 编辑

### B. 权限说明

#### 用户端接口
- 除登录接口外，其他接口均需携带 JWT Token
- Token 在请求头中携带: `Authorization: Bearer {token}`

#### 后台管理接口
- 所有 `/admin/*` 接口需要管理员权限
- 不同角色拥有不同的操作权限

### C. 分页说明

#### 默认值
- `page` 默认为 1
- `page_size` 默认为 20，最大为 100

#### 返回格式
```json
{
  "total": 总记录数,
  "page": 当前页码,
  "page_size": 每页数量,
  "list": []
}
```

---

**文档版本**: v1.0  
**最后更新**: 2025-11-18  
**维护者**: 开发团队