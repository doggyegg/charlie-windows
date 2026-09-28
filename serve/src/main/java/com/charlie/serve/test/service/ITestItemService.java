package com.charlie.serve.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.serve.test.dto.TestItemRequest;
import com.charlie.serve.test.dto.TestItemVO;
import com.charlie.serve.test.entity.TestItem;

import java.util.List;

/**
 * 测试条目业务接口。
 * 继承 MyBatis-Plus 的 IService 获得通用 CRUD 能力（save/getById/list/removeById 等），
 * 并声明本项目面向 VO 的业务方法，供 Controller 调用。
 */
public interface ITestItemService extends IService<TestItem> {

	/**
	 * 查询全部条目（按 id 倒序）并转换为 VO 列表。
	 *
	 * @return 条目 VO 列表，无数据时返回空列表
	 */
	List<TestItemVO> findAllVO();

	/**
	 * 按主键查询单条并转换为 VO。
	 *
	 * @param id 主键
	 * @return 条目 VO
	 * @throws org.springframework.web.server.ResponseStatusException 记录不存在时抛出 404
	 */
	TestItemVO findByIdVO(Long id);

	/**
	 * 根据请求创建条目并返回 VO，创建时间由本方法赋值。
	 *
	 * @param request 创建请求
	 * @return 新建条目 VO（含自增主键）
	 */
	TestItemVO createVO(TestItemRequest request);

	/**
	 * 根据请求更新指定条目并返回 VO。
	 *
	 * @param id      主键
	 * @param request 更新请求
	 * @return 更新后条目 VO
	 * @throws org.springframework.web.server.ResponseStatusException 记录不存在时抛出 404
	 */
	TestItemVO updateVO(Long id, TestItemRequest request);

	/**
	 * 按主键删除条目。
	 *
	 * @param id 主键
	 * @throws org.springframework.web.server.ResponseStatusException 记录不存在时抛出 404
	 */
	void deleteById(Long id);
}
