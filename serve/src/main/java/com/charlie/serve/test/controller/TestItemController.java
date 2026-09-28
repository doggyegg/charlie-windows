package com.charlie.serve.test.controller;

import com.charlie.serve.test.dto.TestItemRequest;
import com.charlie.serve.test.dto.TestItemVO;
import com.charlie.serve.test.service.ITestItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试条目 REST 控制器。
 * 对外接口路径与改造前完全一致（/api/test/items），仅将依赖从具体 Service 类
 * 改为面向 ITestItemService 接口注入，符合国内主流的"面向接口编程"约定。
 */
@RestController
@RequestMapping("/api/test/items")
public class TestItemController {

    private final ITestItemService service;

    public TestItemController(ITestItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<TestItemVO> list() {
        return service.findAllVO();
    }

    @GetMapping("/{id}")
    public TestItemVO get(@PathVariable Long id) {
        return service.findByIdVO(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestItemVO create(@Valid @RequestBody TestItemRequest request) {
        return service.createVO(request);
    }

    @PutMapping("/{id}")
    public TestItemVO update(@PathVariable Long id, @Valid @RequestBody TestItemRequest request) {
        return service.updateVO(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
