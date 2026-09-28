package com.charlie.serve.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TestItemRequest(
        @NotBlank(message = "name 不能为空")
        @Size(max = 50, message = "name 最长 50 个字符")
        String name,

        @Size(max = 200, message = "description 最长 200 个字符")
        String description,

        Boolean done
) {
}
