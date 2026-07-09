<div align="center">

# Fish OJ

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.9-1693E2?style=flat-square&logo=mybatis&logoColor=white)](https://baomidou.com/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.40.0-FF6B35?style=flat-square&logo=shield&logoColor=white)](https://sa-token.dev33.cn/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.x-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![Hutool](https://img.shields.io/badge/Hutool-5.8.46-00BFB3?style=flat-square&logo=java&logoColor=white)](https://hutool.cn/)

在线判题系统后端

</div>

## 运行

```bash
mysql -uroot -p < sql/create.sql

mvn spring-boot:run
```

默认端口 `8080`，MySQL / Redis 密码改 `src/main/resources/application.yaml`。

## 模块

```
auth     Sa-Token 自定义权限
common   配置 / 异常 / 统一返回
user     用户 + 做题统计
problem  题目 + 测试用例
tag      标签
judge    提交 + 判题明细
```

## API

统一返回 `{ code, message, data }`，登录后 Header 带 `token: <uuid>`。

**匿名可访问**

| Method | Path | 说明 |
|---|---|---|
| POST | `/api/user/register` | 注册 |
| POST | `/api/user/login` | 登录，返回 token |
| GET  | `/api/problem/list` | 题目列表，可按 tagId 过滤 |
| GET  | `/api/problem/{id}` | 题目详情 |
| GET  | `/api/tag/list` | 标签列表 |

**登录**

| Method | Path | 说明 |
|---|---|---|
| GET  | `/api/user/me` | 当前用户信息 |
| POST | `/api/user/logout` | 登出 |
| POST | `/api/judge/submit` | 提交代码，返回 submit id |
| GET  | `/api/judge/submit/{id}` | 提交详情（仅本人/管理员） |
| GET  | `/api/judge/submit/{id}/cases` | 每个用例的判题明细 |
| GET  | `/api/judge/submit/list` | 我的提交列表 |
| GET  | `/api/user/problem/list` | 我的做题记录 |
| GET  | `/api/user/problem/{problemId}` | 单题统计 |

**管理员（`admin` 角色）**

| Prefix | 说明 |
|---|---|
| `/api/admin/user` | 用户管理 |
| `/api/admin/problem` | 题目管理 |
| `/api/admin/test-case` | 测试用例管理 |
| `/api/admin/tag` | 标签 + 题目绑定 |

## 约定

**权限分层**：
- 匿名 → 注册/登录/题目列表/题目详情/标签列表
- 登录 → 提交/自己的判题结果
- 本人或管理员 → 提交详情/判题明细（Service 校验）
- 管理员 → `/api/admin/*`（`@SaCheckRole` 拦截）

**错误码**：见 `common/exception/ErrorCode.java`，统一经 `GlobalExceptionHandler` 转 `Result` 返回。

## 判题

`SubmitServiceImpl.submit` 只做"记录 pending 提交"，真实判题沙箱**待接入**。接入后需要：
1. 异步更新 submit 状态/得分/时间/内存
2. 写入 `judge_case` 明细
3. 调用 `userProblemService.recordSubmit` 累计统计
