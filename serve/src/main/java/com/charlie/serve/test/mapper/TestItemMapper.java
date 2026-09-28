package com.charlie.serve.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charlie.serve.test.entity.TestItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测试条目 Mapper 接口。
 * 继承 MyBatis-Plus 的 BaseMapper 即获得针对 TestItem 的单表 CRUD 能力
 * （insert/deleteById/updateById/selectById/selectList 等），无需手写 SQL；
 * 复杂查询可在本接口内追加方法名派生查询或 @Select 注解 SQL。
 */
@Mapper
public interface TestItemMapper extends BaseMapper<TestItem> {
}
