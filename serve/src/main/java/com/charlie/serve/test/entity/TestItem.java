package com.charlie.serve.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.Instant;

/**
 * 测试条目实体，映射 test_item 表。
 * 由原 JPA @Entity 改造为 MyBatis-Plus 实体：去除 jakarta.persistence 注解，
 * 改用 @TableName 指定表名、@TableId 指定主键策略；createdAt 的赋值逻辑移至 Service 层。
 */
@TableName("test_item")
public class TestItem {

	@TableId(type = IdType.AUTO)
	private Long id;

	private String name;

	private String description;

	private boolean done;

	private Instant createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
