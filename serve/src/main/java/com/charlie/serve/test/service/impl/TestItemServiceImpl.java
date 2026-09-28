package com.charlie.serve.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.serve.test.dto.TestItemRequest;
import com.charlie.serve.test.dto.TestItemVO;
import com.charlie.serve.test.entity.TestItem;
import com.charlie.serve.test.mapper.TestItemMapper;
import com.charlie.serve.test.service.ITestItemService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * 测试条目业务实现。
 * 继承 ServiceImpl&lt;TestItemMapper, TestItem&gt; 复用 MyBatis-Plus 通用 CRUD，
 * 在此基础上实现面向 VO 的业务逻辑：倒序查询、字段校验赋值、创建时间填充与 404 处理。
 */
@Service
public class TestItemServiceImpl extends ServiceImpl<TestItemMapper, TestItem> implements ITestItemService {

	/**
	 * 查询全部条目（按 id 倒序）并转换为 VO 列表。
	 */
	@Override
	public List<TestItemVO> findAllVO() {
		return list(new LambdaQueryWrapper<TestItem>().orderByDesc(TestItem::getId)).stream()
				.map(this::toVO)
				.toList();
	}

	/**
	 * 按主键查询单条并转换为 VO，不存在时抛出 404。
	 */
	@Override
	public TestItemVO findByIdVO(Long id) {
		return toVO(getEntityOrThrow(id));
	}

	/**
	 * 创建条目：赋值请求字段与创建时间后落库，返回含自增主键的 VO。
	 */
	@Override
	@Transactional
	public TestItemVO createVO(TestItemRequest request) {
		TestItem item = new TestItem();
		apply(item, request);
		item.setCreatedAt(Instant.now());
		save(item);
		return toVO(item);
	}

	/**
	 * 更新条目：先按主键取出（不存在抛 404），覆盖请求字段后落库，返回最新 VO。
	 */
	@Override
	@Transactional
	public TestItemVO updateVO(Long id, TestItemRequest request) {
		TestItem item = getEntityOrThrow(id);
		apply(item, request);
		updateById(item);
		return toVO(item);
	}

	/**
	 * 按主键删除条目，不存在时抛出 404。
	 */
	@Override
	@Transactional
	public void deleteById(Long id) {
		if (getById(id) == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id=" + id + " 的记录不存在");
		}
		removeById(id);
	}

	/**
	 * 按主键获取实体，不存在则抛出 404。
	 */
	private TestItem getEntityOrThrow(Long id) {
		TestItem item = getById(id);
		if (item == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id=" + id + " 的记录不存在");
		}
		return item;
	}

	/**
	 * 将请求字段应用到实体：name/description 做 trim，description 空值兜底为空串，done 空值兜底为 false。
	 */
	private void apply(TestItem item, TestItemRequest request) {
		item.setName(request.name().trim());
		item.setDescription(request.description() == null ? "" : request.description().trim());
		item.setDone(Boolean.TRUE.equals(request.done()));
	}

	/**
	 * 实体转 VO。
	 */
	private TestItemVO toVO(TestItem item) {
		return new TestItemVO(item.getId(), item.getName(), item.getDescription(), item.isDone(), item.getCreatedAt());
	}
}
