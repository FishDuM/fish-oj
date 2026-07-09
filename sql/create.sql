CREATE DATABASE IF NOT EXISTS fish_oj DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fish_oj;

-- ====================== 用户/题目/测试 ======================

CREATE TABLE user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname    VARCHAR(50)  COMMENT '昵称',
    email       VARCHAR(100) COMMENT '邮箱',
    role        VARCHAR(10)  NOT NULL DEFAULT 'user' COMMENT '角色: admin-管理员, user-普通用户',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE problem (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '题目ID',
    title           VARCHAR(200) NOT NULL COMMENT '题目标题',
    description     TEXT COMMENT '题目描述',
    input_desc      TEXT COMMENT '输入描述',
    output_desc     TEXT COMMENT '输出描述',
    sample_input    TEXT COMMENT '样例输入',
    sample_output   TEXT COMMENT '样例输出',
    difficulty      VARCHAR(10) DEFAULT 'easy' COMMENT '难度: easy-简单, medium-中等, hard-困难',
    time_limit_ms   INT         DEFAULT 1000   COMMENT '时间限制(毫秒)',
    memory_limit_kb INT         DEFAULT 262144 COMMENT '内存限制(KB)',
    create_user_id  BIGINT      COMMENT '创建人ID',
    status          INT         DEFAULT 1 COMMENT '逻辑删除: 1-正常, 0-已删除',
    create_time     DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_problem_create_user (create_user_id),
    INDEX idx_problem_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- 题目测试用例（真判题核心数据）
CREATE TABLE problem_test_case (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '测试用例ID',
    problem_id  BIGINT NOT NULL COMMENT '所属题目ID',
    input       TEXT    NOT NULL COMMENT '输入数据',
    output      TEXT    NOT NULL COMMENT '期望输出',
    score       INT     DEFAULT 10 COMMENT '该用例分值',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_tc_problem (problem_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目测试用例';

-- ====================== 标签系统 ======================

CREATE TABLE tag (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    name        VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名',
    create_time DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目标签字典';

CREATE TABLE problem_tag (
    problem_id BIGINT NOT NULL COMMENT '题目ID',
    tag_id     BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (problem_id, tag_id),
    INDEX idx_pt_tag (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目-标签关联';

-- ====================== 用户做题统计 ======================

CREATE TABLE user_problem (
    user_id          BIGINT NOT NULL COMMENT '用户ID',
    problem_id       BIGINT NOT NULL COMMENT '题目ID',
    status           VARCHAR(20) COMMENT '状态: none-未做, attempted-尝试过, ac-已通过',
    best_score       INT DEFAULT 0 COMMENT '最高得分',
    submit_count     INT DEFAULT 0 COMMENT '提交次数',
    ac_count         INT DEFAULT 0 COMMENT '通过次数',
    last_submit_time DATETIME COMMENT '最近提交时间',
    PRIMARY KEY (user_id, problem_id),
    INDEX idx_up_problem (problem_id),
    INDEX idx_up_last_submit (last_submit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-题目做题统计';

-- ====================== 判题/提交 ======================

-- 提交记录主表
CREATE TABLE submit (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '提交ID',
    user_id        BIGINT       NOT NULL COMMENT '提交用户ID',
    problem_id     BIGINT       NOT NULL COMMENT '题目ID',
    language       VARCHAR(20)  NOT NULL COMMENT '编程语言',
    code           TEXT         NOT NULL COMMENT '提交代码',
    status         VARCHAR(30)  DEFAULT 'pending' COMMENT '状态: pending-待判, judging-判题中, accepted-通过, wrong_answer-答案错误, time_limit_exceeded-超时, memory_limit_exceeded-超内存, compile_error-编译错误, runtime_error-运行错误',
    total_score    INT          DEFAULT 0 COMMENT '总得分',
    time_used_ms   INT          COMMENT '运行时间(毫秒)',
    memory_used_kb INT          COMMENT '运行内存(KB)',
    error_message  TEXT         COMMENT '编译/运行错误信息',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_submit_user (user_id),
    INDEX idx_submit_problem (problem_id),
    INDEX idx_submit_create (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提交记录';

-- 每个用例的判题明细
CREATE TABLE judge_case (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '判题明细ID',
    submit_id      BIGINT NOT NULL COMMENT '提交记录ID',
    test_case_id   BIGINT NOT NULL COMMENT '测试用例ID',
    status         VARCHAR(30) COMMENT '该用例状态',
    time_used_ms   INT COMMENT '该用例运行时间(毫秒)',
    memory_used_kb INT COMMENT '该用例运行内存(KB)',
    score          INT DEFAULT 0 COMMENT '该用例得分',
    INDEX idx_jc_submit (submit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用例判题明细';