# API接口文档

## 1. 用户管理
### 概述
用户管理模块负责用户的注册、登录以及个人信息管理。

### 接口
#### 1.1 用户注册
- **请求方式**: POST
- **请求路径**: `/api/user/register`
- **请求参数**:
  - `username`: string
  - `password`: string
- **响应示例**:
  ```json
  {
      "message": "注册成功",
      "userId": "12345"
  }
  ```
- **状态码**:
  - `201`: 注册成功
  - `400`: 参数错误

- **Swagger 注释**:
  ```yaml
  /api/user/register:
    post:
      summary: 用户注册
      parameters:
        - name: username
          in: body
          required: true
          type: string
        - name: password
          in: body
          required: true
          type: string
  ```

#### 1.2 用户登录
- **请求方式**: POST
- **请求路径**: `/api/user/login`
- **请求参数**:
  - `username`: string
  - `password`: string
- **响应示例**:
  ```json
  {
      "token": "abcdef1234567",
      "expiresIn": 3600
  }
  ```
- **状态码**:
  - `200`: 登录成功
  - `401`: 用户名或密码错误

- **Swagger 注释**:
  ```yaml
  /api/user/login:
    post:
      summary: 用户登录
      parameters:
        - name: username
          in: body
          required: true
          type: string
        - name: password
          in: body
          required: true
          type: string
  ```


## 2. 学习资源
### 概述
学习资源模块提供学习材料的上传与获取功能。

### 接口
#### 2.1 获取学习资源
- **请求方式**: GET
- **请求路径**: `/api/resources`
- **响应示例**:
  ```json
  [
      {"id": 1, "title": "数学课程", "url": "http://example.com/resource1"},
      {"id": 2, "title": "物理课程", "url": "http://example.com/resource2"}
  ]
  ```
- **状态码**:
  - `200`: 获取成功

- **Swagger 注释**:
  ```yaml
  /api/resources:
    get:
      summary: 获取学习资源
      responses:
        200:
          description: 成功获取资源
  ```

#### 2.2 上传资源
- **请求方式**: POST
- **请求路径**: `/api/resources/upload`
- **请求参数**:
  - `file`: 文件对象
- **响应示例**:
  ```json
  {
      "message": "上传成功",
      "resourceId": "12345"
  }
  ```
- **状态码**:
  - `201`: 上传成功
  - `400`: 上传失败

- **Swagger 注释**:
  ```yaml
  /api/resources/upload:
    post:
      summary: 上传学习资源
      parameters:
        - name: file
          in: formData
          required: true
          type: file
  ```


## 3. 个性化学习计划
### 概述
个性化学习计划模块允许学生创建和查看自己的学习计划。

### 接口
#### 3.1 创建学习计划
- **请求方式**: POST
- **请求路径**: `/api/study-plan`
- **请求参数**:
  - `title`: string
  - `content`: string
- **响应示例**:
  ```json
  {
      "message": "学习计划创建成功",
      "planId": "12345"
  }
  ```
- **状态码**:
  - `201`: 创建成功
  - `400`: 参数错误

- **Swagger 注释**:
  ```yaml
  /api/study-plan:
    post:
      summary: 创建学习计划
      parameters:
        - name: title
          in: body
          required: true
          type: string
        - name: content
          in: body
          required: true
          type: string
  ```

#### 3.2 获取学习计划
- **请求方式**: GET
- **请求路径**: `/api/study-plan/{id}`
- **响应示例**:
  ```json
  {
      "id": "12345",
      "title": "我的学习计划",
      "content": "详细内容"
  }
  ```
- **状态码**:
  - `200`: 获取成功
  - `404`: 学习计划未找到

- **Swagger 注释**:
  ```yaml
  /api/study-plan/{id}:
    get:
      summary: 获取学习计划
      parameters:
        - name: id
          in: path
          required: true
          type: string
  ```


## 4. 题库
### 概述
题库模块提供获取题目和提交答案的功能。

### 接口
#### 4.1 获取题目
- **请求方式**: GET
- **请求路径**: `/api/questions`
- **响应示例**:
  ```json
  [
      {"questionId": 1, "content": "题目内容1"},
      {"questionId": 2, "content": "题目内容2"}
  ]
  ```
- **状态码**:
  - `200`: 获取成功

- **Swagger 注释**:
  ```yaml
  /api/questions:
    get:
      summary: 获取题目
      responses:
        200:
          description: 成功获取题目
  ```

#### 4.2 提交答案
- **请求方式**: POST
- **请求路径**: `/api/questions/submit`
- **请求参数**:
  - `questionId`: string
  - `answer`: string
- **响应示例**:
  ```json
  {
      "message": "答案提交成功"
  }
  ```
- **状态码**:
  - `200`: 提交成功
  - `400`: 提交失败

- **Swagger 注释**:
  ```yaml
  /api/questions/submit:
    post:
      summary: 提交答案
      parameters:
        - name: questionId
          in: body
          required: true
          type: string
        - name: answer
          in: body
          required: true
          type: string
  ```


## 5. 社区互动
### 概述
社区互动模块允许用户参与讨论并查看讨论内容。

### 接口
#### 5.1 发布讨论
- **请求方式**: POST
- **请求路径**: `/api/discussions`
- **请求参数**:
  - `title`: string
  - `content`: string
- **响应示例**:
  ```json
  {
      "message": "讨论发布成功",
      "discussionId": "12345"
  }
  ```
- **状态码**:
  - `201`: 发布成功
  - `400`: 参数错误

- **Swagger 注释**:
  ```yaml
  /api/discussions:
    post:
      summary: 发布讨论
      parameters:
        - name: title
          in: body
          required: true
          type: string
        - name: content
          in: body
          required: true
          type: string
  ```

#### 5.2 获取讨论
- **请求方式**: GET
- **请求路径**: `/api/discussions`
- **响应示例**:
  ```json
  [
      {"id": 1, "title": "讨论标题1", "content": "讨论内容1"},
      {"id": 2, "title": "讨论标题2", "content": "讨论内容2"}
  ]
  ```
- **状态码**:
  - `200`: 获取成功

- **Swagger 注释**:
  ```yaml
  /api/discussions:
    get:
      summary: 获取讨论
      responses:
        200:
          description: 成功获取讨论
  ```


## 6. 系统管理
### 概述
系统管理模块允许管理员管理用户和查看统计信息。

### 接口
#### 6.1 管理用户
- **请求方式**: PUT
- **请求路径**: `/api/admin/user/{id}`
- **请求参数**:
  - `userId`: string
  - `action`: string
- **响应示例**:
  ```json
  {
      "message": "用户管理成功"
  }
  ```
- **状态码**:
  - `200`: 管理成功
  - `404`: 用户未找到

- **Swagger 注释**:
  ```yaml
  /api/admin/user/{id}:
    put:
      summary: 管理用户
      parameters:
        - name: id
          in: path
          required: true
          type: string
  ```

#### 6.2 查看统计信息
- **请求方式**: GET
- **请求路径**: `/api/admin/statistics`
- **响应示例**:
  ```json
  {
      "totalUsers": 100,
      "activeUsers": 80
  }
  ```
- **状态码**:
  - `200`: 获取成功

- **Swagger 注释**:
  ```yaml
  /api/admin/statistics:
    get:
      summary: 查看统计信息
      responses:
        200:
          description: 成功获取统计信息
  ```

