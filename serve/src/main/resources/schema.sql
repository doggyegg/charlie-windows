-- 测试条目表（替代原 JPA ddl-auto:update 的自动建表）
-- 由 spring.sql.init.mode=always 在应用启动时执行，IF NOT EXISTS 保证幂等
CREATE TABLE IF NOT EXISTS test_item (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(50)  NOT NULL                COMMENT '名称',
    description VARCHAR(200) DEFAULT NULL            COMMENT '描述',
    done        TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '是否完成',
    created_at  DATETIME(6)  NOT NULL                COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='测试条目';
